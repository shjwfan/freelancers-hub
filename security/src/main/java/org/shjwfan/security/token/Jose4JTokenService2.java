package org.shjwfan.security.token;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;
import static org.shjwfan.security.token.PasswordResetTokenService.PasswordResetSummary.ACTUAL_PASSWORD_CLAIM_NAME;
import static org.shjwfan.security.token.PasswordResetTokenService.PasswordResetSummary.CONFIRM_REDIRECT_CLAIM_NAME;
import static org.shjwfan.security.token.PasswordResetTokenService.PasswordResetSummary.DISCARD_REDIRECT_CLAIM_NAME;

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
import org.shjwfan.security.FreelancersHubSecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Jose4JTokenService2 implements PasswordResetTokenService, TokenService {

  private static final Map<Integer, String> ERROR_CODE_2_MESSAGE = Map.of(ErrorCodes.AUDIENCE_INVALID, "token has invalid audience", ErrorCodes.EXPIRED, "token has expired", ErrorCodes.ISSUER_INVALID, "token has invalid issuer");
  private static final String ISSUER = "www.freelancers-hub.org";

  private final FreelancersHubSecurityProperties freelancersHubSecurityProperties;
  private final PasswordResetTokenHolder passwordResetTokenHolder;
  private final TokenHolder tokenHolder;
  private final Key key;
  private final Logger logger;

  public Jose4JTokenService2(FreelancersHubSecurityProperties freelancersHubSecurityProperties, PasswordResetTokenHolder passwordResetTokenHolder, TokenHolder tokenHolder) {
    this.freelancersHubSecurityProperties = freelancersHubSecurityProperties;
    this.passwordResetTokenHolder = passwordResetTokenHolder;
    this.tokenHolder = tokenHolder;

    byte[] bytes = new byte[256];
    new SecureRandom().nextBytes(bytes);

    this.key = new HmacKey(bytes);

    this.logger = LoggerFactory.getLogger(getClass());
  }

  @Override
  public String createPasswordResetToken(PasswordResetSummary summary) {
    String subject = summary.username();
    Map<String, Object> claims = Map.of(ACTUAL_PASSWORD_CLAIM_NAME, summary.actualPassword(), CONFIRM_REDIRECT_CLAIM_NAME, summary.confirmRedirect(), DISCARD_REDIRECT_CLAIM_NAME, summary.discardRedirect());

    String passwordResetTokenAudience = freelancersHubSecurityProperties.getPasswordResetToken().getAudience();
    long passwordResetTokenExpirationMs = freelancersHubSecurityProperties.getPasswordResetToken().getExpirationMs();
    String passwordResetToken = create(passwordResetTokenAudience, passwordResetTokenExpirationMs, subject, claims);

    passwordResetTokenHolder.putPasswordResetToken(subject, passwordResetToken);

    logger.trace("create {} password reset token {}", subject, passwordResetToken);
    return passwordResetToken;
  }

  @Override
  public Token createToken(String subject) {
    String accessTokenAudience = freelancersHubSecurityProperties.getAccessToken().getAudience();
    long accessTokenExpirationMs = freelancersHubSecurityProperties.getAccessToken().getExpirationMs();
    String accessToken = create(accessTokenAudience, accessTokenExpirationMs, subject, Map.of());
    String refreshTokenAudience = freelancersHubSecurityProperties.getRefreshToken().getAudience();
    long refreshTokenExpirationMs = freelancersHubSecurityProperties.getRefreshToken().getExpirationMs();
    String refreshToken = create(refreshTokenAudience, refreshTokenExpirationMs, subject, Map.of());

    Token token = new Token(accessToken, refreshToken);
    tokenHolder.putToken(subject, token);

    logger.trace("create {} token {}", subject, token);
    return token;
  }

  private String create(String audience, long expirationTimeMillis, String subject, Map<String, Object> claims) {
    long issuedAtTimeMillis = System.currentTimeMillis();

    JsonWebSignature jsonWebSignature = new JsonWebSignature();

    JwtClaims jwtClaims = new JwtClaims();
    jwtClaims.setAudience(audience);
    jwtClaims.setExpirationTime(NumericDate.fromMilliseconds(issuedAtTimeMillis + expirationTimeMillis));
    jwtClaims.setIssuedAt(NumericDate.fromMilliseconds(issuedAtTimeMillis));
    jwtClaims.setIssuer(ISSUER);
    jwtClaims.setSubject(subject);

    claims.forEach((claimName, value) -> {
      jwtClaims.setClaim(claimName, value);
    });

    String payload = jwtClaims.toJson();
    jsonWebSignature.setPayload(payload);
    jsonWebSignature.setKey(key);

    jsonWebSignature.setAlgorithmHeaderValue(HMAC_SHA256);

    try {
      return jsonWebSignature.getCompactSerialization();
    } catch (JoseException e) {
      throw new TokenException("can't create %s token " + subject, e);
    }
  }

  @Override
  public PasswordResetSummary verifyPasswordResetToken(String passwordResetToken) {
    JwtClaims jwtClaims = process(freelancersHubSecurityProperties.getPasswordResetToken().getAudience(), passwordResetToken);
    String subject;
    try {
      subject = jwtClaims.getSubject();
    } catch (MalformedClaimException e) {
      throw new IllegalStateException("subject expected");
    }
    String createdPasswordResetToken = passwordResetTokenHolder.removePasswordResetToken(subject).orElse(null);
    if (StringUtils.equals(createdPasswordResetToken, passwordResetToken)) {
      String actualPassword = jwtClaims.getClaimValueAsString(ACTUAL_PASSWORD_CLAIM_NAME);
      if (StringUtils.isBlank(actualPassword)) {
        throw new TokenException(ACTUAL_PASSWORD_CLAIM_NAME + " claim is blank");
      }
      String confirmRedirect = jwtClaims.getClaimValueAsString(CONFIRM_REDIRECT_CLAIM_NAME);
      if (StringUtils.isBlank(confirmRedirect)) {
        throw new TokenException(CONFIRM_REDIRECT_CLAIM_NAME + " claim is blank");
      }
      String discardRedirect = jwtClaims.getClaimValueAsString(DISCARD_REDIRECT_CLAIM_NAME);
      if (StringUtils.isBlank(discardRedirect)) {
        throw new TokenException(DISCARD_REDIRECT_CLAIM_NAME + " claim is blank");
      }
      return new PasswordResetSummary(subject, actualPassword, confirmRedirect, discardRedirect);
    }
    throw new TokenException(subject + " created password reset token doesn't match password reset token");
  }

  @Override
  public String verifyAccessToken(String accessToken) {
    JwtClaims jwtClaims = process(freelancersHubSecurityProperties.getPasswordResetToken().getAudience(), accessToken);
    String subject;
    try {
      subject = jwtClaims.getSubject();
    } catch (MalformedClaimException e) {
      throw new IllegalStateException("subject expected");
    }
    String createdAccessToken = tokenHolder.getToken(subject).map(Token::accessToken).orElse(null);
    if (StringUtils.equals(createdAccessToken, accessToken)) {
      return subject;
    }
    throw new TokenException(subject + " created access token doesn't match access token");
  }

  @Override
  public String verifyRefreshToken(String refreshToken) {
    JwtClaims jwtClaims = process(freelancersHubSecurityProperties.getPasswordResetToken().getAudience(), refreshToken);
    String subject;
    try {
      subject = jwtClaims.getSubject();
    } catch (MalformedClaimException e) {
      throw new IllegalStateException("subject expected");
    }
    String createdRefreshToken = tokenHolder.removeToken(subject).map(Token::refreshToken).orElse(null);
    if (StringUtils.equals(createdRefreshToken, refreshToken)) {
      return subject;
    }
    throw new TokenException(subject + " created refresh token doesn't match refresh token");
  }

  private JwtClaims process(String audience, String token) {
    JwtConsumer jwtConsumer = new JwtConsumerBuilder().setExpectedAudience(audience).setRequireExpirationTime().setExpectedIssuer(ISSUER).setRequireSubject().setVerificationKey(key).build();
    try {
      return jwtConsumer.process(token).getJwtClaims();
    } catch (InvalidJwtException e) {
      handleInvalidJwtException(e);
      throw new TokenException("can't process token " + token, e);
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
