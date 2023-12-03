package org.shjwfan.security.token;

import jakarta.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class ConcurrentMapTokenHolder implements PasswordResetTokenHolder, TokenHolder {

  private final ConcurrentMap<String, String> subject2PasswordResetToken = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Token> subject2Token = new ConcurrentHashMap<>();

  @Override
  public Optional<String> getPasswordResetToken(@Nullable String subject) {
    return Optional.ofNullable(subject2PasswordResetToken.get(subject));
  }

  @Override
  public void putPasswordResetToken(String subject, String passwordResetToken) {
    subject2PasswordResetToken.put(subject, passwordResetToken);
  }

  @Override
  public Optional<String> removePasswordResetToken(@Nullable String subject) {
    return Optional.ofNullable(subject2PasswordResetToken.remove(subject));
  }

  @Override
  public Optional<Token> getToken(@Nullable String subject) {
    return Optional.ofNullable(subject2Token.get(subject));
  }

  @Override
  public void putToken(String subject, Token token) {
    subject2Token.put(subject, token);
  }

  @Override
  public Optional<Token> removeToken(@Nullable String subject) {
    return Optional.ofNullable(subject2Token.remove(subject));
  }
}
