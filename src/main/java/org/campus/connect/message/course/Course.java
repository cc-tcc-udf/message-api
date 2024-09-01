package org.campus.connect.message.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.utils.AbstractEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Course_tb", schema = SchemaConstants.MESSAGES)
public class Course extends AbstractEntity {
  private String name;
  private String description;
  private String abbreviation;
  private Long resp;
  @Column(name = "course_group_id")
  private Long courseGroupId;
  @Column(name = "is_group")
  private Boolean isGroup;
}
