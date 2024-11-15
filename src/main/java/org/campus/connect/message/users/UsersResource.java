package org.campus.connect.message.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.constants.GenericMessages;
import org.campus.connect.message.infra.auth.TokenService;
import org.campus.connect.message.users.records.*;
import org.campus.connect.message.utils.GenericResource;
import org.campus.connect.message.utils.dtos.ReturnObjDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

  @GetMapping("/public/refreshToken")
  @Operation(summary = "Buscar dados usuario", description = "Atualizar o token")
  public ResponseEntity<TokenRefreshDTO> refreshToken(@RequestParam String email) {
    Optional<Users> usr = this.repository.findByEmail(email);
    UsersDTO user = new UsersDTO();
    if (usr.isPresent()) {
      user = service.getUser(usr.get());
    }
    String newToken = tokenService.generateToken(mapper.toEntity(user));
    TokenRefreshDTO dto = new TokenRefreshDTO(email, newToken);
    return ResponseEntity.ok(dto);
  }


  @GetMapping(value = "/public/auth/adm/listResp")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios para o administrador")
  public ReturnObjDTO listResp() {
    try {
      return new ReturnObjDTO(service.findResp(), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @PostMapping("/private/auth/adm/create")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Criar usuarios adm", description = "Para o administrador cadastrar usuarios")
  public ResponseEntity<UsersDTO> createAdmin(@RequestBody UsersDTO user) throws Exception {
    return ResponseEntity.ok().body(service.createUser(user));
  }

  @PostMapping("/private/auth/adm/update")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Atualizar usuarios adm", description = "Para o administrador atualizar usuarios")
  public ResponseEntity<UsersDTO> updateAdim(@RequestBody UsersDTO user) throws Exception {
    return ResponseEntity.ok().body(service.createUser(user));
  }

  @PutMapping("/private/auth/update")
  @Operation(summary = "Atualizar perfil", description = "Para o usuario atualizar o dados do perfil")
  public ReturnObjDTO update(@RequestBody UsersDTO user) throws Exception {
    try {
      return new ReturnObjDTO(service.update(user), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
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
    if (!user.isActive()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("{\"message\": \"Sua conta ainda não foi ativada. Aguarde o e-mail de ativação enviado pelo administrador.\"}");
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

  @PostMapping(value = "/public/auth/mobile/register")
  @Operation(summary = "Cadastro", description = "Para o usuário realizar o registro")
  public ResponseEntity<?> register_mobile(
    @RequestBody RegisterDTO body
  ) throws Exception {
    Optional<Users> usr = this.repository.findByEmail(body.getEmail());
    if (usr.isEmpty()) {
      Users user = this.service.register_mobile(body);
      String token = this.tokenService.generateToken(user);
      ReturnObjDTO objDTO = new ReturnObjDTO(new ResponseDTO(user.getEmail(), token), true);
      return ResponseEntity.ok(objDTO);
    }
    ReturnObjDTO objDTO = new ReturnObjDTO();
    objDTO.setSuccess(false);
    objDTO.setMessage("Cadastro não realizado. O e-mail informado já está em uso.");
    return ResponseEntity.ok(objDTO);
  }


  @PostMapping(value = "/public/auth/register")
  @Operation(summary = "Cadastro", description = "Para o usuário realizar o registro")
  public ResponseEntity<?> register(
    @RequestBody RegisterDTO body,
    @RequestParam(required = false) boolean isMobile
  ) throws Exception {
    Optional<Users> usr = this.repository.findByEmail(body.getEmail());
    if (usr.isEmpty()) {
      Users user = this.service.register(body, isMobile);
      String token = this.tokenService.generateToken(user);
      ReturnObjDTO objDTO = new ReturnObjDTO(isMobile ? new ResponseDTO(user.getEmail(), token) : null, true);
      return ResponseEntity.ok(objDTO);
    }
    ReturnObjDTO objDTO = new ReturnObjDTO();
    objDTO.setSuccess(false);
    objDTO.setMessage("Cadastro não realizado. O e-mail informado já está em uso.");
    return ResponseEntity.ok(objDTO);
  }

  @GetMapping(value = "/public/admin/create")
  @Tag(name = "ADMIN")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Admin", description = "Registrar o usuário admin")
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
