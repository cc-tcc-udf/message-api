package org.campus.connect.message.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.files.File;
import org.campus.connect.message.users.records.RegisterDTO;
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
  private String phone;
  private UUID id_curso;
  @JoinColumn(name = "is_temp_pass")
  private boolean active;  private boolean isTempPass;
  private String temporary_password;

  @ManyToOne
  @JoinColumn(name = "profile_photo_id")
  private File profilePhoto;

  @ManyToOne
  @JoinColumn(name = "cover_photo_id")
  private File coverPhoto;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  @CollectionTable(schema = SchemaConstants.MESSAGES, name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
  private Set<UserRoles> roles = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = SchemaConstants.MESSAGES, name = "user_tokens", joinColumns = @JoinColumn(name = "user_id"))
  private Set<String> token = new HashSet<>();

  public Users(RegisterDTO dto){
    setEmail(dto.getEmail());
    setName(dto.getName());
    setPhone(dto.getPhone());
    setActive(dto.isActive());
    if (dto.getTokens() != null) {
      setToken(new HashSet<>(dto.getTokens()));
    }
  }
}
