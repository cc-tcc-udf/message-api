package org.campus.connect.message.auth.users;

import org.campus.connect.message.constants.Enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
public interface UsersRepository extends JpaRepository<Users, Long> {

  List<Users> findAllByExcluded(Boolean excluded);

  Optional<Users> findByEmail(String email);

  List<Users> findByRolesContains(UserRoles roles);

}
