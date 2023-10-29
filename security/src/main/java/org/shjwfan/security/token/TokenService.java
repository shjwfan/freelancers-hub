package org.shjwfan.security.token;

public interface TokenService {

  Token create(String subject);

  String verifyAccessToken(String accessToken);

  String verifyRefreshToken(String refreshToken);
}
