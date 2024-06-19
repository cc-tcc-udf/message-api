package org.campus.connect.message.auth.users;

import org.campus.connect.message.auth.users.records.RegisterDTO;
import org.campus.connect.message.constants.Enums.Roles_user;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersServiceImpl extends GenericServiceImpl<Users, UsersDTO> implements UsersService {

  private final UsersRepository repository;
  private final UsersMapper mapper;
  private final PasswordEncoder passwordEncoder;

  public UsersServiceImpl(
    UsersRepository repository,
    UsersMapper mapper, PasswordEncoder passwordEncoder
  ) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Optional<Users> findByEmail(String login) {
    return this.repository.findByEmail(login);
  }

  @Override
  public List<UsersDTO> findAll() {
    List<Users> listUsers = this.repository.findAllByExcluded(Boolean.FALSE);
    return this.mapper.toDto(listUsers);
  }

  @Override
  public Users register(RegisterDTO obj) throws Exception {
    Users user = new Users();
    user.setEmail(obj.getEmail());
    user.setName(obj.getName());
    user.setUid(UUID.randomUUID());
    user.setRoles(Collections.singleton(Roles_user.USER));
    user.setPassword(passwordEncoder.encode(obj.getPassword()));
//    user.setFoto_capa(
//      this.arquivoMapper.toEntity(
//        arquivoService.save(new ArquivoDTO())
//      )
//    );
//    user.setFoto_perfil(
//      this.arquivoMapper.toEntity(
//        arquivoService.save(new ArquivoDTO())
//      )
//    );
    this.save(mapper.toDto(user));
    return user;
  }
  @Override
  public UsersDTO adminCreate() throws Exception {
    Users user = new Users();
    user.setEmail("admin@admin.com");
    user.setName("Taui silva Lima");
    user.setUid(UUID.randomUUID());
    user.setRoles(Collections.singleton(Roles_user.ADMIN));
    user.setPassword(passwordEncoder.encode("sousen1902*"));
    return this.save(mapper.toDto(user));
  }

  @Override
  public UsersDTO getUser(Users user) {
    UsersDTO usr = new UsersDTO();
    usr.setEmail(user.getEmail());
    usr.setId(user.getId());
    usr.setName(user.getName());
    usr.setRoles(new ArrayList<>(user.getRoles()));
    usr.setUid(user.getUid());
//    usr.setFoto_capa(arquivoMapper.toDto(user.getFoto_capa()));
//    usr.setFoto_perfil(arquivoMapper.toDto(user.getFoto_perfil()));
    return usr;
  }

  @Override
  public UsersDTO update(final UsersDTO obj) throws Exception {
    Optional<Users> usr = findByEmail(obj.getEmail());
    if (usr.isPresent()) {
      Users user = usr.get();
      obj.setPassword(user.getPassword());
      obj.setUpdatedBy(String.valueOf(user.getUid()));
    }
    return this.save(obj);
  }

}
