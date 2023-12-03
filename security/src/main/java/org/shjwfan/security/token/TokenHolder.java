package org.shjwfan.security.token;

import jakarta.annotation.Nullable;
import java.util.Optional;

public interface TokenHolder {

  Optional<Token> getToken(@Nullable String subject);

  void putToken(String subject, Token token);

  Optional<Token> removeToken(@Nullable String subject);
}
