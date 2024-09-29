package org.campus.connect.message.auth.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.files.File;
import org.campus.connect.message.utils.AbstractEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users_tb", schema = SchemaConstants.MESSAGES)
public class Users extends AbstractEntity {

  private String email;
  private String name;
  private String password;
  private UUID uid;
  private String phone;
  private Long id_curso;
  @ManyToOne
  private File profilePhoto;
  @ManyToOne
  private File coverPhoto;
  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  @CollectionTable(schema = SchemaConstants.MESSAGES, name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
  private Set<UserRoles> roles = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = SchemaConstants.MESSAGES, name = "user_tokens", joinColumns = @JoinColumn(name = "user_id"))
  private Set<String> token = new HashSet<>();

}
