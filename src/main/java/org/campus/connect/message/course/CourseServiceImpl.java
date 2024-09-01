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
    List<Course> courses = isGroup ? this.repository.findByIsGroup(true) : this.repository.findByIsGroupFalse();
    return this.mapper.toDto(courses);
  }

  @Override
  public List<CourseDTO> findAll() {
    List<Course> courses = this.repository.findAll();
    return this.mapper.toDto(courses);
  }

  @Override
  public CourseDTO create(CourseDTO dto) {
    if (!dto.getIsGroup() && dto.getCourseGroupId() != null) {
      Optional<Course> group = repository.findById(dto.getCourseGroupId());
      if (group.isPresent() && group.get().getIsGroup()) {
        throw new IllegalArgumentException("Um grupo não pode ser um curso de outro grupo");
      }
    }
    if (dto.getIsGroup()) {
      dto.setCourseGroupId(null);
    }
    Course course = repository.save(mapper.toEntity(dto));
    return mapper.toDto(course);
  }
}
