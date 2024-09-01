package org.campus.connect.message.course;

import org.campus.connect.message.utils.GenericService;

import java.util.List;

public interface CourseService extends GenericService<CourseDTO> {
  List<CourseDTO> findAll(Boolean isGroup);

  List<CourseDTO> findAll();

  CourseDTO create(CourseDTO dto);
}
