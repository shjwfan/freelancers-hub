package org.shjwfan.security.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ProviderManagerBeanConfiguration {

  @Bean
  public ProviderManager authenticationProvider(UserDetailsService userDetailsService,
                                                PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(daoAuthenticationProvider);
  }
}
