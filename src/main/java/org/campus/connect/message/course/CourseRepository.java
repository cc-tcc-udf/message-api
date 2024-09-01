package org.campus.connect.message.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query("SELECT s FROM Course s WHERE s.courseGroupId = :courseGroupId")
  List<Course> findByCourseGroupId(@Param("courseGroupId") Long courseGroupId);

  @Query("SELECT new org.campus.connect.message.course.SubCourseDTO(s) FROM Course s WHERE s.courseGroupId = :courseGroupId")
  List<SubCourseDTO> findSubsByIdGroup(@Param("courseGroupId") Long courseGroupId);

  @Query("SELECT new org.campus.connect.message.course.CourseDTO(c) FROM Course c WHERE c.isGroup = :isGroup")
  List<CourseDTO> findCourses(@Param("isGroup") Boolean isGroup);

}
