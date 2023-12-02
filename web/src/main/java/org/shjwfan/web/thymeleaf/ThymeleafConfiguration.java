package org.shjwfan.web.thymeleaf;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfiguration {

  @Bean
  public ITemplateResolver appDistTemplateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

    templateResolver.setCharacterEncoding(UTF_8.name());
    templateResolver.setCheckExistence(true);
    templateResolver.setPrefix("classpath:/app/dist/");
    templateResolver.setOrder(0);

    return templateResolver;
  }
}
