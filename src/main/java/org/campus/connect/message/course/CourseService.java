package org.campus.connect.message.course;

import org.campus.connect.message.utils.GenericService;

import java.util.List;

public interface CourseService extends GenericService<CourseDTO> {
  List<CourseDTO> findAll(Boolean isGroup);

  List<CourseDTO> findAll();

  List<CourseDTO> findGroups();

  CourseDTO create(CourseDTO dto) throws Exception;

  CourseDTO update(CourseDTO dto);
}
