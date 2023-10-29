package org.shjwfan.business;

public class WorkException extends RuntimeException {

  public WorkException(String message) {
    super(message);
  }

  public WorkException(String message, Throwable cause) {
    super(message, cause);
  }

  public WorkException(Throwable cause) {
    super(cause);
  }
}
