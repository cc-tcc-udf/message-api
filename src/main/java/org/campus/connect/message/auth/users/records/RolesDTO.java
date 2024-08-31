package org.campus.connect.message.auth.users.records;


import org.campus.connect.message.constants.Enums.UserRoles;

import java.util.List;

public record RolesDTO(String email, List<UserRoles> roles) {

}
