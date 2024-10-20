package org.campus.connect.message.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.course.Course;
import org.campus.connect.message.utils.AbstractEntityDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SubCourseDTO extends AbstractEntityDTO {
  private String name;
  private String description;
  private String abbreviation;
  private UsersDTO resp;
  private Long courseGroupId;
  private String siglaGroup;
  private Boolean isGroup;

  public SubCourseDTO(Course obj) {
    setId(obj.getId());
    setExcluded(obj.getExcluded());
    setCreated(obj.getCreated());
    setUpdated(obj.getUpdated());
    this.name = obj.getName();
    this.description = obj.getDescription();
    this.abbreviation = obj.getAbbreviation();
    this.resp = new UsersDTO(obj.getResp());
    this.courseGroupId = obj.getCourseGroupId();
    this.isGroup = obj.getIsGroup();
  }

  public SubCourseDTO(final Long id, final String name, final String abbreviation) {
    this.setId(id);
    this.setName(name + "(" + abbreviation + ")");
  }
}