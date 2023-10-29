package org.shjwfan.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class TokenOncePerRequestFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String ACCESS_TOKEN_PREFIX = "Bearer ";

  private @Autowired TokenService tokenService;
  private @Autowired UserDetailsService userDetailsService;

  @SneakyThrows
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
    String accessToken = StringUtils.substringAfter(authorizationHeader, ACCESS_TOKEN_PREFIX);
    if (accessToken != null) {
      String username = tokenService.verifyAccessToken(accessToken);

      UserDetails user = userDetailsService.loadUserByUsername(username);

      var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

      SecurityContext context = SecurityContextHolder.getContext();
      context.setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }
}
