package org.shjwfan.business;

public class FreelancerException extends RuntimeException {

  public FreelancerException(String message) {
    super(message);
  }

  public FreelancerException(String message, Throwable cause) {
    super(message, cause);
  }

  public FreelancerException(Throwable cause) {
    super(cause);
  }
}
