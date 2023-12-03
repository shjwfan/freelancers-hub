package org.shjwfan.security;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("freelancers-hub.security")
@Configuration
public class FreelancersHubSecurityProperties {

  private CorsProperties cors = new CorsProperties();
  private TokenProperties accessToken = new TokenProperties();
  private TokenProperties refreshToken = new TokenProperties();
  private TokenProperties passwordResetToken = new TokenProperties();

  public CorsProperties getCors() {
    return cors;
  }

  public void setCors(CorsProperties cors) {
    this.cors = cors;
  }

  public TokenProperties getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(TokenProperties accessToken) {
    this.accessToken = accessToken;
  }

  public TokenProperties getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(TokenProperties refreshToken) {
    this.refreshToken = refreshToken;
  }

  public TokenProperties getPasswordResetToken() {
    return passwordResetToken;
  }

  public void setPasswordResetToken(TokenProperties passwordResetToken) {
    this.passwordResetToken = passwordResetToken;
  }

  public static class CorsProperties {

    private long maxAgeMillis = Duration.ofSeconds(90).toMillis();

    public long getMaxAgeMillis() {
      return maxAgeMillis;
    }

    public void setMaxAgeMillis(long maxAgeMillis) {
      this.maxAgeMillis = maxAgeMillis;
    }
  }

  public static class TokenProperties {

    private String audience = "www.freelancers-hub.org";
    private long expirationMs = Duration.ofSeconds(90).toMillis();

    public String getAudience() {
      return audience;
    }

    public void setAudience(String audience) {
      this.audience = audience;
    }

    public long getExpirationMs() {
      return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
      this.expirationMs = expirationMs;
    }
  }
}
