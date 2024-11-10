package org.campus.connect.message.infra.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.campus.connect.message.users.Users;
import org.campus.connect.message.utils.dtos.UserDetailDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

  private final ObjectMapper objectMapper;

  public AuthUserService(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String getCurrentUser() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof final Users user) {
      try {
        UserDetailDTO usr = new UserDetailDTO(user);
        return objectMapper.writeValueAsString(usr);
      } catch (Exception e) {
        return "{\"user\":\"system\"}";
      }
    }
    return "{\"user\":\"system\"}";
  }
}
