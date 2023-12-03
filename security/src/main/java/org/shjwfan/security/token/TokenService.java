package org.shjwfan.security.token;

public interface TokenService {

  Token createToken(String subject);

  String verifyAccessToken(String accessToken);

  String verifyRefreshToken(String refreshToken);
}
