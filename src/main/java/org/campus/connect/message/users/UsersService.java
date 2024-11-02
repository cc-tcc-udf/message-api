package org.campus.connect.message.users;

import org.campus.connect.message.users.records.RegisterDTO;
import org.campus.connect.message.utils.GenericService;

import java.util.List;
import java.util.Optional;

public interface UsersService extends GenericService<UsersDTO> {

  List<UsersDTO> findAll();

  Users register(RegisterDTO body, boolean isMobile) throws Exception;

  Optional<Users> findByEmail(String login);

  UsersDTO adminCreate() throws Exception;

  UsersDTO createUser(UsersDTO usr) throws Exception;

  UsersDTO getUser(Users users);

  UsersDTO update(UsersDTO user) throws Exception;

  List<UsersDTO> findResp();
}
