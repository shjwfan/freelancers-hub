package org.shjwfan.security.token;

import jakarta.annotation.Nullable;
import java.util.Optional;

public interface TokenHolder {

  Optional<Token> get(@Nullable String subject);

  void put(String subject, Token token);

  Optional<Token> remove(@Nullable String subject);
}
