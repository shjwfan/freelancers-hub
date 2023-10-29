package org.shjwfan.security.token;

import jakarta.annotation.Nullable;

public interface TokenHolder {

  @Nullable
  Token get(String subject);

  void put(String subject, Token token);

  @Nullable
  Token remove(String subject);
}
