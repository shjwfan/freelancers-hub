package org.shjwfan.security.web;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.shjwfan.security.token.Token;
import org.shjwfan.security.token.TokenException;
import org.shjwfan.security.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRestController {

  private @Autowired TokenService tokenService;
  private @Autowired AuthenticationManager authenticationManager;
  private @Autowired UserDetailsService userDetailsService;

  @GetMapping("/api/v1/login")
  public Token token(@Nonnull @RequestParam("username") String username, @Nonnull @RequestParam("password") String password) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      throw new TokenException("username or password is blank");
    }

    var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    return tokenService.create(username);
  }

  @GetMapping("/api/v1/login/refresh")
  public Token tokenRefresh(@Nonnull @RequestParam("refreshToken") String currentRefreshToken) {
    if (StringUtils.isBlank(currentRefreshToken)) {
      throw new TokenException("refresh token is blank");
    }

    String username = tokenService.verifyRefreshToken(currentRefreshToken);

    UserDetails user = userDetailsService.loadUserByUsername(username);

    var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    return tokenService.create(username);
  }
}
