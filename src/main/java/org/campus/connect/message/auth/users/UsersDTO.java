package org.campus.connect.message.auth.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.Roles_user;
import org.campus.connect.message.files.ArquivoDTO;
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
  private String telefone;
  private ArquivoDTO foto_perfil;
  private ArquivoDTO foto_capa;
  private List<Roles_user> roles;

}
