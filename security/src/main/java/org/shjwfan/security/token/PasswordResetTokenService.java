package org.shjwfan.security.token;

public interface PasswordResetTokenService {

  record PasswordResetSummary(String username, String actualPassword, String confirmRedirect, String discardRedirect) {

    public static final String ACTUAL_PASSWORD_CLAIM_NAME = "actualPassword";
    public static final String CONFIRM_REDIRECT_CLAIM_NAME = "confirmRedirect";
    public static final String DISCARD_REDIRECT_CLAIM_NAME = "discardRedirect";
  }

  String createPasswordResetToken(PasswordResetSummary summary);

  PasswordResetSummary verifyPasswordResetToken(String passwordResetToken);
}
