package org.campus.connect.message.utils.dtos;

import lombok.Data;
import org.campus.connect.message.users.Users;

import java.util.UUID;

@Data
public class UserDetailDTO {
  private Long id;
  private UUID uid;
  private String name;
  private String email;
  private String phone;
  private boolean active;

  public UserDetailDTO(Users usr) {
    this.id = usr.getId();
    this.uid = usr.getUid();
    this.name = usr.getName();
    this.email = usr.getEmail();
    this.phone = usr.getPhone();
    this.active = usr.isActive();
  }
}
