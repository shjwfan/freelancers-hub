package org.shjwfan.security.web.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfigurationSourceBeanConfiguration {

  private final long maxAgeMilliseconds;

  public CorsConfigurationSourceBeanConfiguration(
      @Value("${freelancers-hub.cors-max-age-ms:90000}") int maxAgeMilliseconds) {
    this.maxAgeMilliseconds = maxAgeMilliseconds;
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

    corsConfiguration.setMaxAge(maxAgeMilliseconds);

    return corsConfiguration;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
    UrlBasedCorsConfigurationSource urlBased = new UrlBasedCorsConfigurationSource();

    urlBased.registerCorsConfiguration("/api/v1/**", corsConfiguration);

    return urlBased;
  }
}
