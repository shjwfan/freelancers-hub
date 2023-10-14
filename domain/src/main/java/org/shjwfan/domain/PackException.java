package org.shjwfan.domain;

public class PackException extends RuntimeException {

  public PackException(String message) {
    super(message);
  }

  public PackException(String message, Throwable cause) {
    super(message, cause);
  }

  public PackException(Throwable cause) {
    super(cause);
  }
}
