package org.shjwfan.security.token;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;
import static org.jose4j.jwt.consumer.ErrorCodes.AUDIENCE_INVALID;
import static org.jose4j.jwt.consumer.ErrorCodes.EXPIRED;
import static org.jose4j.jwt.consumer.ErrorCodes.ISSUER_INVALID;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Objects;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Jose4jTokenService implements TokenService {

  private static final String ALGORITHM_HEADER_VALUE = HMAC_SHA256;

  private final long accessTokenExpirationMilliseconds;
  private final long refreshTokenExpirationMilliseconds;
  private final String audience;
  private final String issuer;
  private final Key verificationKey;
  private final TokenHolder tokenHolder;

  public Jose4jTokenService(@Value("${freelancers-hub.access-token-expiration-ms:45000}") int accessTokenExpirationMilliseconds,
                            @Value("${freelancers-hub.refresh-token-expiration-ms:90000}") int refreshTokenExpirationMilliseconds,
                            @Value("${freelancers-hub.token-audience}") String audience,
                            @Value("${freelancers-hub.token-issuer}") String issuer,
                            TokenHolder tokenHolder) {
    this.accessTokenExpirationMilliseconds = accessTokenExpirationMilliseconds;
    this.refreshTokenExpirationMilliseconds = refreshTokenExpirationMilliseconds;
    this.audience = audience;
    this.issuer = issuer;

    byte[] bytes = new byte[256];
    new SecureRandom().nextBytes(bytes);

    this.verificationKey = new HmacKey(bytes);
    this.tokenHolder = tokenHolder;
  }

  @Override
  public Token create(String subject) {
    try {
      Instant createdAt = Instant.now();

      String accessToken = getCompactSerializationUsingRSA(createTokenClaimsJsonPayload(subject, createdAt, accessTokenExpirationMilliseconds), verificationKey);
      String refreshToken = getCompactSerializationUsingRSA(createTokenClaimsJsonPayload(subject, createdAt, refreshTokenExpirationMilliseconds), verificationKey);

      Token token = new Token(accessToken, refreshToken);
      tokenHolder.put(subject, token);

      return token;
    } catch (JoseException e) {
      String message = e.getMessage();
      throw new TokenException("can't create token" + (Objects.isNull(message) ? "" : ", cause " + message.toLowerCase()));
    }
  }

  private String getCompactSerializationUsingRSA(String payload, Key key) throws JoseException {
    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload(payload);
    jws.setKey(key);
    jws.setAlgorithmHeaderValue(ALGORITHM_HEADER_VALUE);
    return jws.getCompactSerialization();
  }

  private String createTokenClaimsJsonPayload(String subject, Instant createdAt, long tokenExpirationMilliseconds) {
    JwtClaims jwtClaims = new JwtClaims();
    jwtClaims.setAudience(audience);

    NumericDate expirationTime = NumericDate.fromMilliseconds(createdAt.toEpochMilli() + tokenExpirationMilliseconds);
    jwtClaims.setExpirationTime(expirationTime);

    jwtClaims.setIssuer(issuer);
    jwtClaims.setIssuedAtToNow();
    jwtClaims.setSubject(subject);
    return jwtClaims.toJson();
  }

  @Override
  public String verifyAccessToken(String accessToken) {
    String subject = verifyToken(accessToken);
    Token token = tokenHolder.get(subject);
    if (Objects.isNull(token) || !Objects.equals(accessToken, token.accessToken())) {
      throw new TokenException("can't verify token, already created token is not equals current token");
    }
    return subject;
  }

  @Override
  public String verifyRefreshToken(String refreshToken) {
    String subject = verifyToken(refreshToken);
    Token token = tokenHolder.get(subject);
    if (Objects.isNull(token) || !Objects.equals(refreshToken, token.refreshToken())) {
      throw new TokenException("can't verify token, already created token is not equals current token");
    }
    return subject;
  }

  private String verifyToken(String token) {
    try {
      JwtConsumer jwtConsumer = new JwtConsumerBuilder()
          .setExpectedAudience(audience)
          .setRequireExpirationTime()
          .setExpectedIssuer(issuer)
          .setRequireSubject()
          .setVerificationKey(verificationKey)
          .build();
      JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
      return jwtClaims.getSubject();
    } catch (InvalidJwtException e) {
      boolean hasInvalidAudience = e.hasErrorCode(AUDIENCE_INVALID);
      if (hasInvalidAudience) {
        throw new TokenException("can't verify token, token has invalid audience");
      }
      boolean hasExpired = e.hasErrorCode(EXPIRED);
      if (hasExpired) {
        throw new TokenException("can't verify token, token has expired");
      }
      boolean hasInvalidIssuer = e.hasErrorCode(ISSUER_INVALID);
      if (hasInvalidIssuer) {
        throw new TokenException("can't verify token, token has invalid issuer");
      }
      String message = e.getMessage();
      throw new TokenException("can't verify token" + (Objects.isNull(message) ? "" : ", cause " + message.toLowerCase()));
    } catch (Exception e) {
      String message = e.getMessage();
      throw new TokenException("can't verify token" + (Objects.isNull(message) ? "" : ", cause " + message.toLowerCase()));
    }
  }
}
