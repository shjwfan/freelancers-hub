package org.shjwfan.web;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.shjwfan.security.token.TokenException;
import org.shjwfan.security.token.TokenExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginRestControllerAdvice {

  private @Autowired TokenExceptionHandler tokenExceptionHandler;

  @ExceptionHandler(TokenException.class)
  public void handleTokenException(@Nonnull TokenException e, @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response) throws IOException {
    tokenExceptionHandler.handleTokenException(e, request, response);
  }
}
