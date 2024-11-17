package org.campus.connect.message.course;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.campus.connect.message.course.dto.SubCourseDTO;
import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CourseDTO extends AbstractEntityDTO {
  private String name;
  private String description;
  private String abbreviation;
  private UsersDTO resp;
  private UUID courseGroupId;
  private Boolean isGroup;
  private List<SubCourseDTO> courses;

  public CourseDTO(Course c) {
    setId(c.getId());
    setExcluded(c.getExcluded());
    setCreated(c.getCreated());
    setUpdated(c.getUpdated());
    this.name = c.getName();
    this.description = c.getDescription();
    this.abbreviation = c.getAbbreviation();
    if (c.getResp() != null) {
      this.resp = new UsersDTO(c.getResp());
    }
    this.courseGroupId = c.getCourseGroupId();
    this.isGroup = c.getIsGroup();
  }

  public CourseDTO(final UUID id, final String name, final String abbreviation, final UsersDTO resp) {
    this.setId(id);
    this.setName(name + "(" + abbreviation + ")");
    this.setResp(resp);
  }

  public CourseDTO(final SubCourseDTO c) {
    setId(c.getId());
    setExcluded(c.getExcluded());
    setCreated(c.getCreated());
    setUpdated(c.getUpdated());
    this.name = c.getName();
    this.description = c.getDescription();
    this.abbreviation = c.getAbbreviation();
    this.resp = c.getResp();
    this.courseGroupId = c.getCourseGroupId();
    this.isGroup = c.getIsGroup();
  }
}
