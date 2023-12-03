package org.shjwfan.web.restcontrollers.login;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRestController {

  private @Autowired TokenService tokenService;
  private @Autowired AuthenticationManager authenticationManager;
  private @Autowired UserDetailsService userDetailsService;

  @PostMapping("/api/v1/login")
  public LoginResponseBody login(@RequestBody LoginRequestBody requestBody) {
    String username = requestBody.username();
    String password = requestBody.password();

    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      throw new TokenException("username or actualPassword is blank");
    }

    var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    Token token = tokenService.createToken(username);
    return new LoginResponseBody(token.accessToken(), token.refreshToken());
  }

  @PostMapping("/api/v1/login/refresh")
  public LoginResponseBody loginRefresh(@RequestBody LoginRefreshRequestBody requestBody) {
    String currentRefreshToken = requestBody.currentRefreshToken();

    if (StringUtils.isBlank(currentRefreshToken)) {
      throw new TokenException("refresh token is blank");
    }

    String username = tokenService.verifyRefreshToken(currentRefreshToken);

    UserDetails user = userDetailsService.loadUserByUsername(username);

    var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    Token token = tokenService.createToken(username);
    return new LoginResponseBody(token.accessToken(), token.refreshToken());
  }
}
