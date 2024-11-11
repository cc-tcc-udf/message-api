package org.campus.connect.message.course;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.users.Users;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.utils.AbstractEntity;

import java.util.UUID;

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
  @Column(name = "course_group_id")
  private UUID courseGroupId;
  @Column(name = "is_group")
  private Boolean isGroup;
  @ManyToOne
  @JoinColumn(name = "resp", referencedColumnName = "id")
  private Users resp;
}
