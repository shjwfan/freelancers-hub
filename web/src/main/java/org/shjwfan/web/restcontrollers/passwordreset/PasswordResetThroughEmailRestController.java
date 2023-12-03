package org.shjwfan.web.restcontrollers.passwordreset;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.shjwfan.mail.MailService;
import org.shjwfan.security.core.userdetails.EmailBasedUserDetails;
import org.shjwfan.security.core.userdetails.EmailBasedUserDetailsService;
import org.shjwfan.security.core.userdetails.EmailNotFoundException;
import org.shjwfan.security.crypto.password.PasswordReseter;
import org.shjwfan.security.token.PasswordResetTokenService;
import org.shjwfan.security.token.PasswordResetTokenService.PasswordResetSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@RestController
public class PasswordResetThroughEmailRestController {

  private static final String PASSWORD_RESET_EMAIL_SUBJECT = "Freelancers Hub Password Reset";
  private static final String PASSWORD_RESET_EMAIL_CONTENT = "password-reset.html";
  private static final String PASSWORD_RESET_TOKEN_PARAM = "passwordResetToken";

  private @Autowired MailService mailService;
  private @Autowired EmailBasedUserDetailsService emailBasedUserDetailsService;
  private @Autowired PasswordReseter passwordReseter;
  private @Autowired PasswordResetTokenService passwordResetTokenService;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private @Autowired PasswordEncoder passwordEncoder;
  private @Autowired ITemplateEngine templateEngine;

  @PostMapping("/api/v1/password-reset/email/ask")
  public void askPasswordResetThroughEmail(@RequestBody AskPasswordResetThroughEmailRequestBody requestBody, @Nonnull HttpServletRequest request) {
    String email = requestBody.email();
    if (StringUtils.isBlank(email)) {
      throw new PasswordResetException("email is blank", HttpServletResponse.SC_BAD_REQUEST);
    }
    String actualPassword = requestBody.actualPassword();
    if (StringUtils.isBlank(actualPassword)) {
      throw new PasswordResetException("actualPassword is blank", HttpServletResponse.SC_BAD_REQUEST);
    }
    String confirmRedirect = requestBody.confirmRedirect();
    if (StringUtils.isBlank(confirmRedirect)) {
      throw new PasswordResetException("confirmRedirect is blank", HttpServletResponse.SC_BAD_REQUEST);
    }
    String discardRedirect = requestBody.discardRedirect();
    if (StringUtils.isBlank(discardRedirect)) {
      throw new PasswordResetException("discardRedirect is blank", HttpServletResponse.SC_BAD_REQUEST);
    }

    PasswordResetSummary summary = new PasswordResetSummary(loadUser(email).getUsername(), passwordEncoder.encode(actualPassword), confirmRedirect, discardRedirect);
    String passwordResetToken = passwordResetTokenService.createPasswordResetToken(summary);

    Context context = createContext(request, passwordResetToken);
    mailService.send(email, PASSWORD_RESET_EMAIL_SUBJECT, templateEngine.process(PASSWORD_RESET_EMAIL_CONTENT, context));
  }

  @GetMapping("/api/v1/password-reset/email/confirm")
  public void confirmPasswordResetThroughEmail(@RequestParam("passwordResetToken") String passwordResetToken, @Nonnull HttpServletResponse response) {
    PasswordResetSummary summary = passwordResetTokenService.verifyPasswordResetToken(passwordResetToken);

    passwordReseter.reset(summary.username(), summary.actualPassword());

    logger.trace("{} confirm password reset", summary.username());
    String confirmRedirect = summary.confirmRedirect();
    try {
      response.sendRedirect(confirmRedirect);
    } catch (IOException e) {
      throw new PasswordResetException("can't redirect to " + confirmRedirect, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/api/v1/password-reset/email/discard")
  public void discardPasswordResetThroughEmail(@RequestParam("passwordResetToken") String passwordResetToken, @Nonnull HttpServletResponse response) {
    PasswordResetSummary summary = passwordResetTokenService.verifyPasswordResetToken(passwordResetToken);

    logger.trace("{} discard password reset", summary.username());
    String discardRedirect = summary.discardRedirect();
    try {
      response.sendRedirect(discardRedirect);
    } catch (IOException e) {
      throw new PasswordResetException("can't redirect to " + discardRedirect, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private EmailBasedUserDetails loadUser(String email) {
    try {
      return emailBasedUserDetailsService.loadUserByEmail(email);
    } catch (EmailNotFoundException e) {
      throw new PasswordResetException("user with email " + email + " was not found", HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private Context createContext(HttpServletRequest request, String passwordResetToken) {
    Context context = new Context();

    UriComponents confirmResetUri = UriComponentsBuilder.newInstance().scheme(request.getScheme()).host(request.getServerName()).port(request.getServerPort())
        .path("/api/v1/password-reset/email/confirm").queryParam(PASSWORD_RESET_TOKEN_PARAM, passwordResetToken).build();
    context.setVariable("confirmResetUri", confirmResetUri.toUriString());
    UriComponents discardResetUri = UriComponentsBuilder.newInstance().scheme(request.getScheme()).host(request.getServerName()).port(request.getServerPort())
        .path("/api/v1/password-reset/email/discard").queryParam(PASSWORD_RESET_TOKEN_PARAM, passwordResetToken).build();
    context.setVariable("discardResetUri", discardResetUri.toUriString());

    String userAgent = Optional.ofNullable(request.getHeader("User-Agent"))
        .orElseThrow(() -> new PasswordResetException("\"User-Agent\" header is blank", HttpServletResponse.SC_BAD_REQUEST));
    context.setVariable("userAgent", userAgent);

    return context;
  }
}
