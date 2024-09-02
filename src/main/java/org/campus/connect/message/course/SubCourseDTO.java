package org.campus.connect.message.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.utils.AbstractEntityDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubCourseDTO extends AbstractEntityDTO {
  private String name;
  private String description;
  private String abbreviation;
  private Long resp;
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
    this.resp = obj.getResp();
    this.courseGroupId = obj.getCourseGroupId();
    this.isGroup = obj.getIsGroup();
  }
}