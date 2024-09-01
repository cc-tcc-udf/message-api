package org.campus.connect.message.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

  List<Course> findAllByExcluded(Boolean excluded);

  List<Course> findByIsGroupFalse();

  List<Course> findByIsGroup(Boolean isGroup);
}
