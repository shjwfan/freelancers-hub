package org.shjwfan.security.token;

import jakarta.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class ConcurrentMapTokenHolder implements TokenHolder {

  private final ConcurrentMap<String, Token> subject2Token = new ConcurrentHashMap<>();

  @Override
  public Optional<Token> get(@Nullable String subject) {
    return Optional.ofNullable(subject2Token.get(subject));
  }

  @Override
  public void put(String subject, Token token) {
    subject2Token.put(subject, token);
  }

  @Override
  public Optional<Token> remove(@Nullable String subject) {
    return Optional.ofNullable(subject2Token.remove(subject));
  }
}
