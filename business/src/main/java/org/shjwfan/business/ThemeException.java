package org.shjwfan.business;

public class ThemeException extends RuntimeException {

  public ThemeException(String message) {
    super(message);
  }

  public ThemeException(String message, Throwable cause) {
    super(message, cause);
  }

  public ThemeException(Throwable cause) {
    super(cause);
  }
}
