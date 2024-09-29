package org.campus.connect.message.infra;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.campus.connect.message.auth.TokenService;
import org.campus.connect.message.auth.users.Users;
import org.campus.connect.message.auth.users.UsersRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityFilter extends OncePerRequestFilter {


  private final TokenService tokenService;
  private final UsersRepository usersRepository;

  public SecurityFilter(final TokenService tokenService, final UsersRepository usersRepository) {
    this.tokenService = tokenService;
    this.usersRepository = usersRepository;
  }

  @Override
  protected void doFilterInternal(
    final @NonNull HttpServletRequest request,
    final @NonNull HttpServletResponse response,
    final @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    var token = this.recoverToken(request);
    var login = this.tokenService.validateToken(token);

    if (login != null) {
      Users user = usersRepository.findByEmail(login)
        .orElseThrow(() -> new RuntimeException("User Not Found"));
      Set<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
        .collect(Collectors.toSet());

      var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }


  private String recoverToken(final HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null) return null;
    return authHeader.replace("Bearer ", "");
  }
}
