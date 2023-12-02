package org.shjwfan.mail;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(value = "freelancers-hub.started-at-mail-enable", havingValue = "enable")
@Service
public class StartedAtMailCommandLineRunner implements CommandLineRunner {

  private static final DateTimeFormatter STARTED_AT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss");

  private @Autowired MailService mailService;
  private @Value("${freelancers-hub.started-at-mail}") String mail = "";

  @Override
  public void run(String... args) {
    if (StringUtils.isBlank(mail)) {
      return;
    }

    ZonedDateTime startedAt = ZonedDateTime.now();

    String text = String.format("Freelancers Hub Started at %s", STARTED_AT_DATE_TIME_FORMATTER.format(startedAt));
    mailService.send(mail, text, text);
  }
}
