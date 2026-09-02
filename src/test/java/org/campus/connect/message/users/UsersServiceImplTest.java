package org.campus.connect.message.users;

import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.course.CourseService;
import org.campus.connect.message.infra.auth.AuthUserService;
import org.campus.connect.message.users.records.RegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import org.campus.connect.message.course.CourseServiceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

  @Mock
  private UsersRepository repository;

  @Mock
  private UsersMapper mapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private CourseServiceImpl courseService;

  @Mock
  private AuthUserService authUserService;

  @InjectMocks
  private UsersServiceImpl usersService;

  private RegisterDTO registerDTO;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(usersService, "authUserService", authUserService);

    registerDTO = new RegisterDTO();
    registerDTO.setName("Maria Silva");
    registerDTO.setEmail("maria@campusconnect.com");
    registerDTO.setPassword("senhaSegura123");
    registerDTO.setRoles(List.of(UserRoles.USER));
  }

  @Test
  @DisplayName("Deve registrar novo usuário web criptografando a senha")
  void shouldRegisterWebUserWithEncodedPassword() throws Exception {
    when(passwordEncoder.encode("senhaSegura123")).thenReturn("$2a$10$encodedHash123");
    when(mapper.toDto(any(Users.class))).thenReturn(new UsersDTO());
    when(mapper.toEntity(any(UsersDTO.class))).thenReturn(new Users());
    when(authUserService.getCurrentUser()).thenReturn("system");

    Users created = usersService.register(registerDTO, false);

    assertNotNull(created);
    assertEquals("maria@campusconnect.com", created.getEmail());
    assertEquals("$2a$10$encodedHash123", created.getPassword());
    assertTrue(created.getRoles().contains(UserRoles.USER));
    verify(passwordEncoder, times(1)).encode("senhaSegura123");
    verify(repository, times(1)).saveAndFlush(any());
  }

  @Test
  @DisplayName("Deve registrar usuário mobile já com status ativo e role USER padrão")
  void shouldRegisterMobileUserAsActiveWithDefaultRole() throws Exception {
    when(passwordEncoder.encode("senhaSegura123")).thenReturn("$2a$10$encodedHashMobile");
    when(mapper.toDto(any(Users.class))).thenReturn(new UsersDTO());
    when(mapper.toEntity(any(UsersDTO.class))).thenReturn(new Users());
    when(authUserService.getCurrentUser()).thenReturn("mobile-system");

    Users created = usersService.register_mobile(registerDTO);

    assertNotNull(created);
    assertTrue(created.isActive());
    assertEquals(Collections.singleton(UserRoles.USER), created.getRoles());
    assertEquals("$2a$10$encodedHashMobile", created.getPassword());
  }

  @Test
  @DisplayName("Deve listar professores disponíveis como responsáveis que ainda não possuem curso associado")
  void shouldReturnAvailableProfessorsNotAssignedToCourses() {
    UUID prof1Id = UUID.randomUUID();
    UUID prof2Id = UUID.randomUUID();

    Users prof1 = new Users();
    prof1.setId(prof1Id);
    prof1.setName("Prof. Carlos");

    Users prof2 = new Users();
    prof2.setId(prof2Id);
    prof2.setName("Prof. Ana");

    when(repository.findByRolesContainsAndActive(UserRoles.PROF, true)).thenReturn(List.of(prof1, prof2));

    CourseDTO course = new CourseDTO();
    UsersDTO resp = new UsersDTO();
    resp.setId(prof1Id);
    course.setResp(resp);

    when(courseService.findAll()).thenReturn(List.of(course));

    List<UsersDTO> availableResps = usersService.findResp();

    assertNotNull(availableResps);
    assertEquals(1, availableResps.size());
    assertEquals(prof2Id, availableResps.get(0).getId());
  }
}
