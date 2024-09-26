package org.campus.connect.message.auth.users.records;

import lombok.Data;

import java.util.List;

@Data
public class RegisterDTO {
  private String name;
  private String email;
  private String password;
  private String phone;
  private List<String> tokens;
}
