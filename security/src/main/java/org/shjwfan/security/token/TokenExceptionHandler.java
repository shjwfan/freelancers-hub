package org.shjwfan.security.token;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class TokenExceptionHandler {

  public void handleTokenException(@Nonnull TokenException e, @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    String message = e.getMessage();
    if (Objects.isNull(message)) {
      return;
    }

    response.getWriter().write("{\"message\":\"" + message + "\""); // jackson?
  }
}
