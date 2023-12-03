package org.shjwfan.web.restcontrollers.passwordreset;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PasswordResetThroughEmailRestControllerAdvice {

  private @Autowired PasswordResetExceptionHandler passwordResetExceptionHandler;

  @ExceptionHandler(PasswordResetException.class)
  public void handlePasswordResetException(@Nonnull PasswordResetException e, @Nonnull HttpServletResponse response) throws IOException {
    passwordResetExceptionHandler.handlePasswordResetException(e, response);
  }
}
