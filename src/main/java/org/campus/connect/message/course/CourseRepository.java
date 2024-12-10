package org.campus.connect.message.course;

import org.campus.connect.message.course.dto.SubCourseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

  @Query("SELECT new org.campus.connect.message.course.dto.SubCourseDTO(s) " +
    "FROM Course s " +
    "WHERE s.courseGroupId = :courseGroupId AND s.excluded = false " +
    "ORDER BY s.name")
  List<SubCourseDTO> findSubsByIdGroup(@Param("courseGroupId") UUID courseGroupId);

  @Query("SELECT new org.campus.connect.message.course.dto.SubCourseDTO(s) " +
    "FROM Course s " +
    "WHERE s.courseGroupId IS NOT NULL AND s.isGroup = false AND s.excluded = false " +
    "ORDER BY s.name")
  List<SubCourseDTO> findAllSubs();

  @Query("SELECT new org.campus.connect.message.course.CourseDTO(c) " +
    "FROM Course c " +
    "WHERE c.isGroup = :isGroup AND c.excluded = false " +
    "ORDER BY c.name")
  List<CourseDTO> findCourses(@Param("isGroup") Boolean isGroup);

  @Query("SELECT new org.campus.connect.message.course.CourseDTO(c) " +
    "FROM Course c " +
    "WHERE c.isGroup = false AND c.courseGroupId IS NULL AND c.excluded = false " +
    "ORDER BY c.name")
  List<CourseDTO> findCoursesNoGrouped();

  @Query("SELECT c " +
    "FROM Course c " +
    "WHERE c.isGroup = true AND c.excluded = false " +
    "ORDER BY c.name")
  List<Course> findAllByIsGroupIsTrue();

  @Query("SELECT new org.campus.connect.message.course.CourseDTO(c) " +
    "FROM Course c " +
    "WHERE c.id = :id AND c.excluded = false")
  CourseDTO findCourseById(@Param("id") UUID id);

  @Query("SELECT new org.campus.connect.message.course.CourseDTO(c) " +
    "FROM Course c " +
    "WHERE c.resp.id = :id AND c.excluded = false " +
    "ORDER BY c.name")
  List<CourseDTO> findCoursesResp(@Param("id") UUID id);
}
