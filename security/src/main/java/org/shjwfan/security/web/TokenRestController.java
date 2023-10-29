package org.shjwfan.security.web;

import jakarta.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;
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
public class TokenRestController {

  private final ConcurrentHashMap<String, String> username2RefreshToken = new ConcurrentHashMap<>();
  private @Autowired TokenService tokenService;
  private @Autowired AuthenticationManager authenticationManager;
  private @Autowired UserDetailsService userDetailsService;

  @GetMapping("/api/v1/token")
  public Token token(@Nonnull @RequestParam("username") String username, @Nonnull @RequestParam("password") String password) {
    var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    String accessToken = tokenService.signAccessToken(username);
    String refreshToken = tokenService.signRefreshToken(username);

    username2RefreshToken.put(username, refreshToken);
    return new Token(accessToken, refreshToken);
  }

  @GetMapping("/api/v1/token/refresh")
  public Token tokenRefresh(@Nonnull @RequestParam("refreshToken") String currentRefreshToken) {
    String username = tokenService.verifyRefreshToken(currentRefreshToken);

    if (!currentRefreshToken.equals(username2RefreshToken.remove(username))) {
      throw new TokenException("unknown refresh token" + username);
    }

    UserDetails user = userDetailsService.loadUserByUsername(username);

    var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    String accessToken = tokenService.signAccessToken(username);
    String refreshToken = tokenService.signRefreshToken(username);

    username2RefreshToken.put(username, refreshToken);
    return new Token(accessToken, refreshToken);
  }
}
