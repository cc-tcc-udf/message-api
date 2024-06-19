package org.campus.connect.message.auth.users.records;


import org.campus.connect.message.constants.Enums.Roles_user;

import java.util.List;

public record RolesDTO(String email, List<Roles_user> roles) {

}
