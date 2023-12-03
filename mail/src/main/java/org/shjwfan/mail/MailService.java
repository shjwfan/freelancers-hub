package org.shjwfan.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  private static final boolean MAIL_SMTP_AUTH = true;
  private static final boolean MAIL_SMTP_STARTTLS_ENABLE = true;

  private final Properties properties;
  private final String username;
  private final String password;
  private final Logger logger;

  public MailService(SmtpConfigurationProperties smtp) {
    properties = new Properties();
    properties.put("mail.smtp.auth", Boolean.toString(MAIL_SMTP_AUTH));
    properties.put("mail.smtp.starttls.enable", Boolean.toString(MAIL_SMTP_STARTTLS_ENABLE));
    properties.put("mail.smtp.host", smtp.getHost());
    properties.put("mail.smtp.port", smtp.getPort());
    username = smtp.getUsername();
    password = smtp.getPassword();
    logger = LoggerFactory.getLogger(getClass());
  }

  public void send(String recipient, String subject, String content) {
    Session session = getSessionInstance();

    // from "noreply@..."?
    String from = username;

    CompletableFuture.runAsync(() -> {
      try {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        message.setRecipient(RecipientType.TO, new InternetAddress(recipient));

        message.setSubject(subject.trim());
        message.setContent(content.trim(), "text/html");

        Transport.send(message);

        logger.trace("send email to {}, subject {}, content {}", recipient, subject, content);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }).exceptionally(e -> {
      logger.trace("can't send email to {}", recipient, e);
      return null;
    });
  }

  private Session getSessionInstance() {
    return Session.getInstance(properties, new Authenticator() {

      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
  }
}
