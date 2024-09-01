package org.campus.connect.message.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO extends AbstractEntityDTO {
  private String name;
  private String description;
  private String abbreviation;
  private Long resp;
  private Long courseGroupId;
  private Boolean isGroup;
  private List<Object> courses;
}
