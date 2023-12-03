package org.shjwfan.web.restcontrollers.passwordreset;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public void handlePasswordResetException(@Nonnull PasswordResetException e, @Nonnull HttpServletResponse response) throws IOException {
    logger.trace(e.getMessage(), e);
    response.setContentType("application/json");
    response.setStatus(e.getStatus());

    String message = e.getMessage();
    if (message != null) {
      response.getWriter().write("{\"message\":\"" + message + "\""); // jackson?
    }
  }
}
