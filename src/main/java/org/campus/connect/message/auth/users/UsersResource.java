package org.campus.connect.message.auth.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.auth.TokenService;
import org.campus.connect.message.auth.users.records.LoginDTO;
import org.campus.connect.message.auth.users.records.RegisterDTO;
import org.campus.connect.message.auth.users.records.ResponseDTO;
import org.campus.connect.message.auth.users.records.RolesDTO;
import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.constants.GenericMessages;
import org.campus.connect.message.responseReturn.ReturnObjDTO;
import org.campus.connect.message.utils.GenericResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api")
@Tag(name = "Users", description = "Gerenciamento de Usuários")
public class UsersResource extends GenericResource<UsersDTO, UsersResource> {

  private final UsersService service;
  private final PasswordEncoder passwordEncoder;
  private final UsersRepository repository;
  private final TokenService tokenService;
  private final UsersMapper mapper;

  public UsersResource(
    UsersService service,
    UsersRepository repository,
    PasswordEncoder passwordEncoder,
    TokenService tokenService, UsersMapper mapper) {
    super(service, "api/");
    this.service = service;
    this.repository = repository;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
    this.mapper = mapper;
  }

  @GetMapping(value = "/private/auth/adm/list")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios para o administrador")
  public ReturnObjDTO listUsers() {
    ReturnObjDTO obj = new ReturnObjDTO();
    try {
      List<UsersDTO> users = service.findAll();
      obj.setData(users);
      obj.setSuccess(Boolean.TRUE);
      obj.setMessage(GenericMessages.ResponseSuccess);
    } catch (Exception e) {
      obj.setSuccess(Boolean.FALSE);
      obj.setMessage(GenericMessages.ResponseError);
    }
    return obj;
  }

  @GetMapping(value = "/public/auth/adm/listResp")
//  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios para o administrador")
  public ReturnObjDTO listResp() {
    ReturnObjDTO obj = new ReturnObjDTO();
    try {
      List<UsersDTO> users = service.findResp();
      obj.setData(users);
      obj.setSuccess(Boolean.TRUE);
      obj.setMessage(GenericMessages.ResponseSuccess);
    } catch (Exception e) {
      obj.setSuccess(Boolean.FALSE);
      obj.setMessage(GenericMessages.ResponseError);
    }
    return obj;
  }

  @PostMapping("/public/auth/create")
  // @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Criar usuarios adm", description = "Para o administrador cadastrar usuarios")
  public ResponseEntity<UsersDTO> create(@RequestBody UsersDTO user) throws Exception {
    user.setUid(UUID.randomUUID());
    super.createObject(user);
    return ResponseEntity.ok().body(user);
  }

  @PutMapping("/private/auth/update")
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "Atualizar perfil", description = "Para o usuario atualizar o dados do perfil")
  public ResponseEntity<UsersDTO> update(@RequestBody UsersDTO user) throws Exception {
    return ResponseEntity.ok(service.update(user));
  }

  @PostMapping("/public/auth/login")
  @Operation(summary = "Login", description = "Para o usuario efetuar o login, retornando o token necessario na busca dos dados em '/private/auth/getUser'")
  public ResponseEntity<?> login(@RequestBody LoginDTO body) {
    Optional<Users> optionalUser = this.repository.findByEmail(body.email());
    if (optionalUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Usuário não cadastrado!\"}");
    }

    Users user = optionalUser.get();
    if (!passwordEncoder.matches(body.password(), user.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Senha incorreta!\"}");

    }

    String token = this.tokenService.generateToken(user);
    return ResponseEntity.ok(new ResponseDTO(user.getEmail(), token));
  }

  @GetMapping("/private/auth/getUser")
  @Operation(summary = "Buscar dados usuario", description = "Apos login, buscar dados do usuario com o token")
  public ResponseEntity<UsersDTO> getUser(@RequestParam String email) {
    Optional<Users> usr = this.repository.findByEmail(email);
    UsersDTO user = new UsersDTO();
    if (usr.isPresent()) {
      user = service.getUser(usr.get());
    }
    return ResponseEntity.ok(user);

  }

  @PostMapping(value = "/public/auth/register")
  @Operation(summary = "Cadastro", description = "Para o usuario realizar o registro")
  public ResponseEntity<?> register(@RequestBody RegisterDTO body) throws Exception {
    Optional<Users> usr = this.repository.findByEmail(body.getEmail());
    if (usr.isEmpty()) {
      Users user = this.service.register(body);
      String token = this.tokenService.generateToken(user);
      return ResponseEntity.ok(new ResponseDTO(user.getEmail(), token));
    }
    return ResponseEntity.badRequest().build();
  }


  @GetMapping(value = "/public/admin/create")
  @Tag(name = "ADMIN")
  @Operation(summary = "Admin", description = "Registrar o usuario admin")
  public ResponseEntity<UsersDTO> getAdmin() throws Exception {
    return ResponseEntity.ok(this.service.adminCreate());
  }

  @PutMapping("/public/auth/user_roler")
  @Operation(summary = "Edição de permissões do usuario", description = "Usuario administrador modifica as rolers do usuario")
  public ResponseEntity<?> roles(@RequestBody RolesDTO dto) throws Exception {
    Optional<Users> usr = this.repository.findByEmail(dto.email());
    if (usr.isPresent()) {
      Users user = usr.get();
      Set<UserRoles> usr_roles = user.getRoles();
      usr_roles.addAll(dto.roles());
      this.service.save(mapper.toDto(user));
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping("/private/auth/delete")
  @Operation(summary = "Deletar", description = "Deletar o usuario")
  public ResponseEntity<?> delete(@RequestParam String email) throws Exception {
    Optional<Users> usr = this.repository.findByEmail(email);
    if (usr.isPresent()) {
      this.service.delete(usr.get().getId());
    }
    return ResponseEntity.noContent().build();
  }
}
