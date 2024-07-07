package org.campus.connect.message.auth.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.Roles_user;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.files.Arquivo;
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
  private String telefone;

  @ManyToOne
  private Arquivo foto_perfil;

  @ManyToOne
  private Arquivo foto_capa;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
  private Set<Roles_user> roles = new HashSet<>();

}
