package org.shjwfan.security.token;

public interface TokenService {

  Token create(String subject);

  @Deprecated(forRemoval = true)
  String signAccessToken(String subject);

  @Deprecated(forRemoval = true)
  String signRefreshToken(String subject);

  String verifyAccessToken(String accessToken);

  String verifyRefreshToken(String refreshToken);
}
