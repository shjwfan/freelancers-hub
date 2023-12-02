package org.shjwfan.security.token;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("freelancers-hub.token")
@Configuration
public class TokenConfigurationProperties {

  private String audience = "www.freelancers-hub";
  private String issuer = "www.freelancers-hub";
  private long accessTokenExpirationMs = Duration.ofSeconds(45).toMillis();
  private long refreshTokenExpirationMs = Duration.ofSeconds(90).toMillis();

  public String getAudience() {
    return audience;
  }

  public void setAudience(String audience) {
    this.audience = audience;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public long getAccessTokenExpirationMs() {
    return accessTokenExpirationMs;
  }

  public void setAccessTokenExpirationMs(long accessTokenExpirationMs) {
    this.accessTokenExpirationMs = accessTokenExpirationMs;
  }

  public long getRefreshTokenExpirationMs() {
    return refreshTokenExpirationMs;
  }

  public void setRefreshTokenExpirationMs(long refreshTokenExpirationMs) {
    this.refreshTokenExpirationMs = refreshTokenExpirationMs;
  }
}
