package org.campus.connect.message.users;

import org.campus.connect.message.constants.Enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, Long> {

  List<Users> findAllByExcluded(Boolean excluded);

  Optional<Users> findByEmail(String email);

  Optional<Users> findByUid(final UUID uid);

  List<Users> findByRolesContainsAndActive(UserRoles roles, Boolean active);

  @Query("SELECT new org.campus.connect.message.users.UsersDTO(u) FROM Users u WHERE u.id = :id")
  UsersDTO getRespById(@Param("id") Long id);
}
