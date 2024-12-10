package org.campus.connect.message.users;

import org.campus.connect.message.constants.Enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {

  List<Users> findAllByExcluded(Boolean excluded);

  Optional<Users> findByEmail(String email);

  List<Users> findByRolesContainsAndActive(UserRoles roles, Boolean active);

  @Query("SELECT u FROM Users u " +
    "WHERE :role MEMBER OF u.roles " +
    "AND u.id_curso = :idCurso " +
    "AND u.excluded = false")
  List<Users> findByRolesAndIdCursoAndExcludedFalse(
    @Param("role") UserRoles role,
    @Param("idCurso") UUID idCurso);
}
