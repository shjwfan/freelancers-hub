package org.shjwfan.security.web;

import java.util.Map;
import org.shjwfan.security.token.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenRestControllerAdvice {

  @ExceptionHandler(TokenException.class)
  public ResponseEntity<Map<String, String>> handleTokenException(TokenException e) {
    String message = e.getMessage();
    Map<String, String> body = Map.of("message", message);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
  }
}
