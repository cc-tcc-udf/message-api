package org.campus.connect.message.course;

import org.campus.connect.message.firebase.FirebaseService;
import org.campus.connect.message.infra.auth.AuthUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

  @Mock
  private CourseRepository repository;

  @Mock
  private CourseMapper mapper;

  @Mock
  private FirebaseService firebaseService;

  @Mock
  private AuthUserService authUserService;

  @InjectMocks
  private CourseServiceImpl courseService;

  private CourseDTO validGroupDTO;
  private CourseDTO validCourseDTO;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(courseService, "authUserService", authUserService);

    validGroupDTO = new CourseDTO();
    validGroupDTO.setName("Engenharias");
    validGroupDTO.setIsGroup(true);

    validCourseDTO = new CourseDTO();
    validCourseDTO.setName("Engenharia de Software");
    validCourseDTO.setIsGroup(false);
  }

  @Test
  @DisplayName("Não deve permitir associar um grupo como filho de outro grupo")
  void shouldThrowExceptionWhenGroupHasCourseGroupId() {
    validGroupDTO.setCourseGroupId(UUID.randomUUID());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      courseService.create(validGroupDTO);
    });

    assertEquals("Um grupo não pode ser atribuído a outro grupo.", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o grupo pai informado não existir")
  void shouldThrowExceptionWhenParentGroupNotFound() {
    UUID nonExistentGroupId = UUID.randomUUID();
    validCourseDTO.setCourseGroupId(nonExistentGroupId);

    when(repository.findById(nonExistentGroupId)).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      courseService.create(validCourseDTO);
    });

    assertEquals("Grupo não encontrado para o ID fornecido.", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção quando o ID pai informado não for do tipo grupo")
  void shouldThrowExceptionWhenParentIsNotGroup() {
    UUID parentId = UUID.randomUUID();
    validCourseDTO.setCourseGroupId(parentId);

    Course invalidParent = new Course();
    invalidParent.setId(parentId);
    invalidParent.setIsGroup(false);

    when(repository.findById(parentId)).thenReturn(Optional.of(invalidParent));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      courseService.create(validCourseDTO);
    });

    assertEquals("O ID fornecido não é de um grupo válido.", exception.getMessage());
  }

  @Test
  @DisplayName("Deve criar curso com sucesso quando associado a um grupo válido")
  void shouldCreateCourseSuccessfullyWithValidGroup() throws Exception {
    UUID parentId = UUID.randomUUID();
    validCourseDTO.setCourseGroupId(parentId);

    Course validParent = new Course();
    validParent.setId(parentId);
    validParent.setIsGroup(true);

    Course courseEntity = new Course();
    courseEntity.setId(UUID.randomUUID());
    courseEntity.setName("Engenharia de Software");

    when(repository.findById(parentId)).thenReturn(Optional.of(validParent));
    when(repository.findCourseById(parentId)).thenReturn(validGroupDTO);
    when(mapper.toEntity(validCourseDTO)).thenReturn(courseEntity);
    when(mapper.toDto(courseEntity)).thenReturn(validCourseDTO);
    when(authUserService.getCurrentUser()).thenReturn("admin@campusconnect.com");

    CourseDTO created = courseService.create(validCourseDTO);

    assertNotNull(created);
    verify(repository, times(1)).saveAndFlush(courseEntity);
  }
}
