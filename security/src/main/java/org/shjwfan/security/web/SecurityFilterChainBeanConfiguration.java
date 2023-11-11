package org.shjwfan.security.web;

import static org.shjwfan.security.core.FreelancersHubAuthority.ADMIN;

import org.shjwfan.security.token.TokenOncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityFilterChainBeanConfiguration {

  private @Autowired TokenOncePerRequestFilter tokenOncePerRequestFilter;
  private @Autowired CorsConfigurationSource corsConfigurationSource;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorizeHttpRequest -> {
          // permit all GET /api/v1/login, GET /api/v1/login/refresh requests
          authorizeHttpRequest.requestMatchers(HttpMethod.GET, "/api/v1/login").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/v1/login/refresh").permitAll()
              .requestMatchers(HttpMethod.GET, "/assets/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/app/**").permitAll()
              .requestMatchers("/api/v1/**").hasAuthority(ADMIN.getAuthority());
        })
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        // disable CSRF, cause all controllers are REST controllers
        .csrf(AbstractHttpConfigurer::disable)
        // disable session management, cause all controllers are REST controllers
        .sessionManagement(AbstractHttpConfigurer::disable);

    http.addFilterBefore(tokenOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
