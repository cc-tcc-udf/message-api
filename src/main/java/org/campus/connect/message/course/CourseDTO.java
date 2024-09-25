package org.campus.connect.message.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.course.dto.SubCourseDTO;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CourseDTO extends AbstractEntityDTO {
  private String name;
  private String description;
  private String abbreviation;
  private Long resp;
  private Long courseGroupId;
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
    this.resp = c.getResp();
    this.courseGroupId = c.getCourseGroupId();
    this.isGroup = c.getIsGroup();
  }

  public CourseDTO(final Long id, final String name, final String abbreviation, final Long resp) {
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
