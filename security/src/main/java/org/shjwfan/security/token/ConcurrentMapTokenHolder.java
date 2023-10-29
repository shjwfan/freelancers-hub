package org.shjwfan.security.token;

import jakarta.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class ConcurrentMapTokenHolder implements TokenHolder {

  private final ConcurrentMap<String, Token> subject2Token = new ConcurrentHashMap<>();

  @Nullable
  @Override
  public Token get(String subject) {
    return subject2Token.get(subject);
  }

  @Override
  public void put(String subject, Token token) {
    subject2Token.put(subject, token);
  }

  @Nullable
  @Override
  public Token remove(String subject) {
    return subject2Token.remove(subject);
  }
}
