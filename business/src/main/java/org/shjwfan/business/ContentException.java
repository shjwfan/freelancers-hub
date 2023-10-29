package org.shjwfan.business;

public class ContentException extends RuntimeException {

  public ContentException(String message) {
    super(message);
  }

  public ContentException(String message, Throwable cause) {
    super(message, cause);
  }

  public ContentException(Throwable cause) {
    super(cause);
  }
}
