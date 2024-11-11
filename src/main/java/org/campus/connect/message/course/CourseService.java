package org.campus.connect.message.course;

import org.campus.connect.message.course.dto.CourseCompleteDTO;
import org.campus.connect.message.utils.GenericService;

import java.util.List;
import java.util.UUID;

public interface CourseService extends GenericService<CourseDTO> {
  List<CourseCompleteDTO> findAll(Boolean isGroup);

  List<CourseDTO> findAll();

  List<CourseDTO> findGroups();

  CourseDTO create(CourseDTO dto) throws Exception;

  CourseDTO update(CourseDTO dto);

  CourseCompleteDTO findCourseById(UUID id);

  List<CourseDTO> getCoursesByResp(UUID id);
}
