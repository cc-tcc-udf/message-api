package org.campus.connect.message.users.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.campus.connect.message.constants.Enums.UserRoles;

import java.util.List;

@Data
public class RegisterDTO {
  @NotBlank(message = "O nome é obrigatório.")
  private String name;

  @NotBlank(message = "O e-mail é obrigatório.")
  @Email(message = "Formato de e-mail inválido.")
  private String email;

  private boolean active;

  @NotBlank(message = "A senha é obrigatória.")
  @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres.")
  private String password;

  private String phone;
  private List<String> tokens;
  private List<UserRoles> roles;
}
