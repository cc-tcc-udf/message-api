package org.campus.connect.message.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CourseCompleteDTO extends AbstractEntityDTO {
  private String name;
  private String description;
  private String abbreviation;
  private UsersDTO resp;
  private UUID courseGroupId;
  private Boolean isGroup;
  private List<SubCourseDTO> courses;

  public CourseCompleteDTO(final CourseDTO courseDTO) {
    setId(courseDTO.getId());
    setExcluded(courseDTO.getExcluded());
    setCreated(courseDTO.getCreated());
    setUpdated(courseDTO.getUpdated());
    this.name = courseDTO.getName();
    this.description = courseDTO.getDescription();
    this.abbreviation = courseDTO.getAbbreviation();
    this.courseGroupId = courseDTO.getCourseGroupId();
    this.resp = courseDTO.getResp();
    this.isGroup = courseDTO.getIsGroup();
    this.courses = courseDTO.getCourses();
  }
}
