package org.shjwfan.security.crypto.password;

public interface PasswordReseter {

  void reset(String subject, String actualPassword);
}
