package org.campus.connect.message.infra.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler exceptionHandler;

  @Mock
  private HttpServletRequest request;

  @Mock
  private MethodArgumentNotValidException validationException;

  @Mock
  private BindingResult bindingResult;

  @BeforeEach
  void setUp() {
    when(request.getRequestURI()).thenReturn("/api/test-endpoint");
  }

  @Test
  @DisplayName("Deve retornar ProblemDetail com status 404 para ResourceNotFoundException")
  void shouldReturn404ForResourceNotFound() {
    ResourceNotFoundException ex = new ResourceNotFoundException("Usuário não encontrado");

    ProblemDetail problem = exceptionHandler.handleResourceNotFound(ex, request);

    assertNotNull(problem);
    assertEquals(HttpStatus.NOT_FOUND.value(), problem.getStatus());
    assertEquals("Recurso não encontrado", problem.getTitle());
    assertEquals("Usuário não encontrado", problem.getDetail());
    assertNotNull(problem.getProperties().get("timestamp"));
  }

  @Test
  @DisplayName("Deve retornar ProblemDetail com status 422 para BusinessException")
  void shouldReturn422ForBusinessException() {
    BusinessException ex = new BusinessException("Regra de negócio violada");

    ProblemDetail problem = exceptionHandler.handleBusinessException(ex, request);

    assertNotNull(problem);
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problem.getStatus());
    assertEquals("Regra de negócio violada", problem.getTitle());
    assertEquals("Regra de negócio violada", problem.getDetail());
  }

  @Test
  @DisplayName("Deve retornar ProblemDetail com status 400 para IllegalArgumentException")
  void shouldReturn400ForIllegalArgumentException() {
    IllegalArgumentException ex = new IllegalArgumentException("Parâmetro inválido fornecido");

    ProblemDetail problem = exceptionHandler.handleIllegalArgumentException(ex, request);

    assertNotNull(problem);
    assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
    assertEquals("Requisição inválida", problem.getTitle());
    assertEquals("Parâmetro inválido fornecido", problem.getDetail());
  }

  @Test
  @DisplayName("Deve retornar ProblemDetail com status 400 e invalidParams para MethodArgumentNotValidException")
  @SuppressWarnings("unchecked")
  void shouldReturn400WithInvalidParamsForValidationErrors() {
    FieldError fieldError = new FieldError("loginDTO", "email", "O e-mail é obrigatório.");
    when(validationException.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    ProblemDetail problem = exceptionHandler.handleValidationException(validationException, request);

    assertNotNull(problem);
    assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
    assertEquals("Erro de validação", problem.getTitle());

    Map<String, String> invalidParams = (Map<String, String>) problem.getProperties().get("invalidParams");
    assertNotNull(invalidParams);
    assertEquals("O e-mail é obrigatório.", invalidParams.get("email"));
  }

  @Test
  @DisplayName("Deve retornar ProblemDetail com status 401 para BadCredentialsException")
  void shouldReturn401ForBadCredentials() {
    BadCredentialsException ex = new BadCredentialsException("Bad credentials");

    ProblemDetail problem = exceptionHandler.handleBadCredentials(ex, request);

    assertNotNull(problem);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.getStatus());
    assertEquals("Falha na autenticação", problem.getTitle());
  }

  @Test
  @DisplayName("Deve retornar ProblemDetail com status 403 para AccessDeniedException")
  void shouldReturn403ForAccessDenied() {
    AccessDeniedException ex = new AccessDeniedException("Access denied");

    ProblemDetail problem = exceptionHandler.handleAccessDenied(ex, request);

    assertNotNull(problem);
    assertEquals(HttpStatus.FORBIDDEN.value(), problem.getStatus());
    assertEquals("Acesso negado", problem.getTitle());
  }

  @Test
  @DisplayName("Deve retornar ProblemDetail com status 500 para Exception genérica")
  void shouldReturn500ForGeneralException() {
    Exception ex = new NullPointerException("Erro inesperado");

    ProblemDetail problem = exceptionHandler.handleGeneralException(ex, request);

    assertNotNull(problem);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problem.getStatus());
    assertEquals("Erro interno do servidor", problem.getTitle());
  }
}
