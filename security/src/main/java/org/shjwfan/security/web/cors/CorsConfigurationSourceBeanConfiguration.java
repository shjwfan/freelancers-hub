package org.shjwfan.security.web.cors;

import org.shjwfan.security.FreelancersHubSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfigurationSourceBeanConfiguration {

  private final FreelancersHubSecurityProperties freelancersHubSecurityProperties;

  public CorsConfigurationSourceBeanConfiguration(FreelancersHubSecurityProperties freelancersHubSecurityProperties) {
    this.freelancersHubSecurityProperties = freelancersHubSecurityProperties;
  }

  @Bean
  public CorsConfiguration corsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();

    // allow origin of vite server
    corsConfiguration.addAllowedOrigin("http://localhost:5173");

    corsConfiguration.addAllowedMethod(HttpMethod.GET.toString());
    corsConfiguration.addAllowedMethod(HttpMethod.POST.toString());

    corsConfiguration.addAllowedHeader("*");

    corsConfiguration.setAllowCredentials(true);

    corsConfiguration.setMaxAge(freelancersHubSecurityProperties.getCors().getMaxAgeMillis());

    return corsConfiguration;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
    UrlBasedCorsConfigurationSource urlBased = new UrlBasedCorsConfigurationSource();

    urlBased.registerCorsConfiguration("/api/v1/**", corsConfiguration);

    return urlBased;
  }
}
