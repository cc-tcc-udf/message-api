package org.campus.connect.message.infra.auth;

import org.campus.connect.message.users.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @InjectMocks
  private TokenService tokenService;

  private Users user;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(tokenService, "secret", "my-super-secret-key-1234567890");
    ReflectionTestUtils.setField(tokenService, "issuer", "campus-connect-test");

    user = new Users();
    user.setEmail("aluno@campusconnect.com");
    user.setName("Aluno Teste");
  }

  @Test
  @DisplayName("Deve gerar token JWT válido para usuário web")
  void shouldGenerateValidTokenForWebUser() {
    String token = tokenService.generateToken(user, false);

    assertNotNull(token);
    assertFalse(token.isBlank());

    String subject = tokenService.validateToken(token);
    assertEquals("aluno@campusconnect.com", subject);
  }

  @Test
  @DisplayName("Deve gerar token JWT válido com expiração móvel para usuário mobile")
  void shouldGenerateValidTokenForMobileUser() {
    String token = tokenService.generateToken(user, true);

    assertNotNull(token);
    assertFalse(token.isBlank());

    String subject = tokenService.validateToken(token);
    assertEquals("aluno@campusconnect.com", subject);
  }

  @Test
  @DisplayName("Deve retornar null ao validar token com formato ou assinatura inválida")
  void shouldReturnNullWhenTokenIsInvalid() {
    String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.signature";

    String subject = tokenService.validateToken(invalidToken);

    assertNull(subject);
  }

  @Test
  @DisplayName("Deve retornar null ao validar token forjado com outra assinatura")
  void shouldReturnNullWhenTokenTamperedWithDifferentSecret() {
    TokenService anotherTokenService = new TokenService();
    ReflectionTestUtils.setField(anotherTokenService, "secret", "different-secret-key-9876543210");
    ReflectionTestUtils.setField(anotherTokenService, "issuer", "campus-connect-test");

    String tokenWithAnotherSecret = anotherTokenService.generateToken(user, false);

    String subject = tokenService.validateToken(tokenWithAnotherSecret);
    assertNull(subject);
  }
}
