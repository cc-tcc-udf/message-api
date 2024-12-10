package org.campus.connect.message.users;

import org.campus.connect.message.users.records.RegisterDTO;
import org.campus.connect.message.utils.GenericService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersService extends GenericService<UsersDTO> {

  UsersDTO getUserById(UUID id);

  List<UsersDTO> findAll();

  Users register(RegisterDTO body, boolean isMobile) throws Exception;

  Users register_mobile(RegisterDTO body) throws Exception;

  Optional<Users> findByEmail(String login);

  UsersDTO adminCreate() throws Exception;

  UsersDTO createUser(UsersDTO usr) throws Exception;

  UsersDTO getUser(Users users);

  UsersDTO getUserMobile(Users users);

  UsersDTO update(UsersDTO user) throws Exception;

  List<UsersDTO> findResp();

  List<UsersDTO> findByCourse(UUID id);
}
