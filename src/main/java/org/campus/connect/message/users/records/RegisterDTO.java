package org.campus.connect.message.users.records;

import lombok.Data;
import org.campus.connect.message.constants.Enums.UserRoles;

import java.util.List;

@Data
public class RegisterDTO {
  private String name;
  private String email;
  private boolean active;
  private String password;
  private String phone;
  private List<String> tokens;
  private List<UserRoles> roles;
}
