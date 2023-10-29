package org.shjwfan.security.token;

import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FusionAuthTokenService implements TokenService {

  private static final String TYPE_CLAIM = "type";

  private final EnumMap<Type, Duration> type2ExpirationDuration = new EnumMap<>(Type.class);
  private final String issuer;
  private final String secret;
  private final TokenHolder tokenHolder;

  public FusionAuthTokenService(@Value("${freelancers-hub.access-token-expiration-millis:30000}") int accessTokenExpirationMillis,
                                @Value("${freelancers-hub.refresh-token-expiration-millis:60000}") int refreshTokenExpirationMillis,
                                @Value("${freelancers-hub.token-issuer}") String issuer,
                                @Value("${freelancers-hub.token-secret}") String secret,
                                TokenHolder tokenHolder) {
    this.type2ExpirationDuration.put(Type.ACCESS, Duration.ofMillis(accessTokenExpirationMillis));
    this.type2ExpirationDuration.put(Type.REFRESH, Duration.ofMillis(refreshTokenExpirationMillis));
    this.issuer = issuer;
    this.secret = secret;
    this.tokenHolder = tokenHolder;
  }

  @Override
  public Token create(String subject) {
    Token token = new Token(signAccessToken(subject), signRefreshToken(subject));
    tokenHolder.put(subject, token);
    return token;
  }

  @Deprecated(forRemoval = true)
  @Override
  public String signAccessToken(String subject) {
    return signToken(subject, Type.ACCESS);
  }

  @Deprecated(forRemoval = true)
  @Override
  public String signRefreshToken(String subject) {
    return signToken(subject, Type.REFRESH);
  }

  @Override
  public String verifyAccessToken(String accessToken) {
    String subject = verify(accessToken, Type.ACCESS);
    Token token = tokenHolder.get(subject);
    if (Objects.isNull(token) || !Objects.equals(accessToken, token.accessToken())) {
      throw new TokenException("already created access token is not equals current access token");
    }
    return subject;
  }

  @Override
  public String verifyRefreshToken(String refreshToken) {
    String subject = verify(refreshToken, Type.REFRESH);
    Token token = tokenHolder.remove(subject);
    if (Objects.isNull(token) || !Objects.equals(refreshToken, token.refreshToken())) {
      throw new TokenException("already created refresh token is not equals current refresh token");
    }
    return subject;
  }

  private String signToken(String subject, Type type) {
    JWT jwt = new JWT();
    jwt.addClaim(TYPE_CLAIM, type);

    jwt.setIssuer(issuer);

    ZonedDateTime issuedAt = ZonedDateTime.now();
    jwt.setIssuedAt(issuedAt);

    ZonedDateTime expiration = issuedAt.plus(type2ExpirationDuration.get(type));
    jwt.setExpiration(expiration);

    jwt.setSubject(subject);
    return JWT.getEncoder().encode(jwt, HMACSigner.newSHA256Signer(secret)); // SHA256
  }

  private String verify(String token, Type type) {
    try {
      JWT jwt = JWT.getDecoder().decode(token, HMACVerifier.newVerifier(secret));

      Map<String, Object> allClaims = jwt.getAllClaims();
      if (allClaims.get(TYPE_CLAIM) != type) {
        throw new TokenException("token type must be " + type.toString());
      }

      return jwt.subject;
    } catch (JWTExpiredException e) {
      throw new TokenException("token is expired");
    } catch (Exception e) {
      String message = e.getMessage();
      if (message != null) {
        throw new TokenException(message);
      } else {
        throw new TokenException("can't verify token");
      }
    }
  }

  private enum Type {
    ACCESS, REFRESH
  }
}
