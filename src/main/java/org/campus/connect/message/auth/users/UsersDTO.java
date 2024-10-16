package org.campus.connect.message.auth.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.files.FileDTO;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UsersDTO extends AbstractEntityDTO {
  private UUID uid;
  private String name;
  private String email;
  private String password;
  private String phone;
  private Long id_curso;
  private FileDTO profilePhoto;
  private FileDTO coverPhoto;
  private List<UserRoles> roles;
  private List<String> tokens;
  private CourseDTO course;

  public UsersDTO(Users usr) {
    setId(usr.getId());
    this.uid = usr.getUid();
    this.name = usr.getName();
    this.email = usr.getEmail();
    this.phone = usr.getPhone();
    this.id_curso = usr.getId_curso();
    this.roles = usr.getRoles().stream().toList();
    if (usr.getProfilePhoto() != null) {
      this.profilePhoto = new FileDTO(usr.getProfilePhoto());
    }
  }
}
