package org.shjwfan.security.web;

import org.shjwfan.security.token.TokenOncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterChainBeanConfiguration {

  private @Autowired TokenOncePerRequestFilter tokenOncePerRequestFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()); // disable CSRF, cause all controllers are REST controllers

    // set stateless session creation policy, cause all controllers are REST controllers
    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeHttpRequestsCustomizer());

    http.addFilterBefore(tokenOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer() {
    return customizer -> customizer
        // permit all GET /api/v1/login, GET /api/v1/login/refresh requests
        .requestMatchers(HttpMethod.GET, "/api/v1/login").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/v1/login/refresh").permitAll()
        .requestMatchers(HttpMethod.GET, "/assets/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/").permitAll()
        .requestMatchers("/api/v1/**").hasAuthority("admin");
  }
}
