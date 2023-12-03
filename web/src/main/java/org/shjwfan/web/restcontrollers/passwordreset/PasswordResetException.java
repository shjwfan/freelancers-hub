package org.shjwfan.web.restcontrollers.passwordreset;

public class PasswordResetException extends RuntimeException {

  private final int status;

  public PasswordResetException(String message, int status) {
    super(message);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
