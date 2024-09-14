package org.campus.connect.message.course;

import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseDTO> implements CourseService {
  final CourseRepository repository;
  final CourseMapper mapper;

  public CourseServiceImpl(final CourseRepository repository, final CourseMapper mapper, final CourseRepository repository1, final CourseMapper mapper1) {
    super(repository, mapper);
    this.repository = repository1;
    this.mapper = mapper1;
  }

  @Override
  public List<CourseDTO> findAll(Boolean isGroup) {
    if (isGroup) {
      List<CourseDTO> list = repository.findCourses(true);
      list.forEach(c -> {
        List<SubCourseDTO> subs = repository.findSubsByIdGroup(c.getId());
        subs.forEach(sub -> {
          sub.setSiglaGroup(c.getAbbreviation());
        });
        c.setCourses(subs);
      });
      list.addAll(this.repository.findCoursesNoGrouped());
      return list;
    } else {
      return this.repository.findCourses(false);
    }
  }

  public CourseDTO findById(final Long idCurso) {
    CourseDTO course = this.repository.findCourseByid(idCurso);
    if (course.getIsGroup()) {
      course.setCourses(this.repository.findSubsByIdGroup(idCurso));
    }
    return course;
  }

  @Override
  public List<CourseDTO> findGroups() {
    List<Course> all = this.repository.findAllByIsGroupIsTrue();
    return all.stream()
      .map(course -> new CourseDTO(course.getId(), course.getName(), course.getAbbreviation()))
      .toList();
  }

  @Override
  public CourseDTO create(CourseDTO dto) throws Exception {
    if (dto.getIsGroup()) {
      if (dto.getCourseGroupId() != null) {
        throw new IllegalArgumentException("Um grupo não pode ser atribuído a outro grupo.");
      }
      dto.setCourseGroupId(null);
    } else {
      if (dto.getCourseGroupId() != null) {
        Optional<Course> group = repository.findById(dto.getCourseGroupId());
        if (group.isPresent()) {
          if (!group.get().getIsGroup()) {
            throw new IllegalArgumentException("O ID fornecido não é de um grupo válido.");
          }
        } else {
          throw new IllegalArgumentException("Grupo não encontrado para o ID fornecido.");
        }
      }
    }
    return save(dto);
  }

  @Override
  public CourseDTO update(final CourseDTO dto) {
    return null;
  }

  @Override
  public List<CourseDTO> findAll() {
    List<Course> courses = this.repository.findAll();
    return this.mapper.toDto(courses);
  }

}
