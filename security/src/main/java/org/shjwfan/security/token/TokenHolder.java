package org.shjwfan.security.token;

public interface TokenHolder {

  Token get(String subject);

  void put(String subject, Token token);

  Token remove(String subject);
}
