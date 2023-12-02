package org.shjwfan.security.token;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Jose4JTokenService2 implements TokenService {

  private static final Map<Integer, String> ERROR_CODE_2_MESSAGE = Map.of(
      ErrorCodes.AUDIENCE_INVALID, "token has invalid audience",
      ErrorCodes.EXPIRED, "token has expired",
      ErrorCodes.ISSUER_INVALID, "token has invalid issuer");

  private final TokenConfigurationProperties tokenConfigurationProperties;
  private final TokenHolder tokenHolder;
  private final Key key;
  private final Logger logger;

  public Jose4JTokenService2(TokenConfigurationProperties tokenConfigurationProperties, TokenHolder tokenHolder) {
    this.tokenConfigurationProperties = tokenConfigurationProperties;
    this.tokenHolder = tokenHolder;

    byte[] bytes = new byte[256];
    new SecureRandom().nextBytes(bytes);

    this.key = new HmacKey(bytes);

    this.logger = LoggerFactory.getLogger(getClass());
  }

  @Override
  public Token create(String subject) {
    long accessTokenExpirationMs = tokenConfigurationProperties.getAccessTokenExpirationMs();
    String accessToken = create(accessTokenExpirationMs, subject);
    long refreshTokenExpirationMs = tokenConfigurationProperties.getRefreshTokenExpirationMs();
    String refreshToken = create(refreshTokenExpirationMs, subject);

    Token token = new Token(accessToken, refreshToken);
    tokenHolder.put(subject, token);

    logger.trace("create {} token {}", subject, token);
    return token;
  }

  private String create(long expirationTimeMillis, String subject) {
    long issuedAtTimeMillis = System.currentTimeMillis();

    JsonWebSignature jsonWebSignature = new JsonWebSignature();

    JwtClaims jwtClaims = new JwtClaims();
    jwtClaims.setAudience(tokenConfigurationProperties.getAudience());
    jwtClaims.setExpirationTime(NumericDate.fromMilliseconds(issuedAtTimeMillis + expirationTimeMillis));
    jwtClaims.setIssuedAt(NumericDate.fromMilliseconds(issuedAtTimeMillis));
    jwtClaims.setIssuer(tokenConfigurationProperties.getIssuer());
    jwtClaims.setSubject(subject);

    String payload = jwtClaims.toJson();
    jsonWebSignature.setPayload(payload);
    jsonWebSignature.setKey(key);

    jsonWebSignature.setAlgorithmHeaderValue(HMAC_SHA256);

    try {
      return jsonWebSignature.getCompactSerialization();
    } catch (JoseException e) {
      String message = String.format("can't create %s token", subject);
      logger.trace(message, e);
      throw new TokenException(message);
    }
  }

  @Override
  public String verifyAccessToken(String accessToken) {
    String subject = getSubject(accessToken);
    String createdAccessToken = tokenHolder.get(subject).map(Token::accessToken).orElse(null);
    if (StringUtils.equals(createdAccessToken, accessToken)) {
      return subject;
    }

    String message = String.format("%s created access token doesn't match access token", subject);
    logger.trace(message);
    throw new TokenException(message);
  }

  @Override
  public String verifyRefreshToken(String refreshToken) {
    String subject = getSubject(refreshToken);
    String createdRefreshToken = tokenHolder.remove(subject).map(Token::refreshToken).orElse(null);
    if (StringUtils.equals(createdRefreshToken, refreshToken)) {
      return subject;
    }

    String message = String.format("%s created refresh token doesn't match refresh token", subject);
    logger.trace(message);
    throw new TokenException(message);
  }

  private String getSubject(String token) {
    try {
      return process(token).getSubject();
    } catch (MalformedClaimException e) {
      throw new IllegalStateException();
    }
  }

  private JwtClaims process(String token) {
    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
        .setExpectedAudience(true, tokenConfigurationProperties.getAudience())
        .setRequireExpirationTime()
        .setExpectedIssuer(true, tokenConfigurationProperties.getIssuer())
        .setRequireSubject()
        .setVerificationKey(key)
        .build();

    try {
      return jwtConsumer.process(token).getJwtClaims();
    } catch (InvalidJwtException e) {
      handleInvalidJwtException(e);
      String message = String.format("can't process token %s", token);
      logger.trace(message, e);
      throw new TokenException(message);
    }
  }

  private void handleInvalidJwtException(InvalidJwtException e) {
    ERROR_CODE_2_MESSAGE.forEach((errorCode, message) -> {
      if (e.hasErrorCode(errorCode)) {
        throw new TokenException(message);
      }
    });
  }
}
