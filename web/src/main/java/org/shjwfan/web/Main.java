package org.shjwfan.web;

import java.util.Map;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAutoConfiguration
@ComponentScans({@ComponentScan("org.shjwfan")})
@Configuration
public class Main implements WebMvcConfigurer {

  public static final Map<String, String[]> RESOURCE_HANDLER_2_RESOURCE_LOCATIONS = Map.of("/assets/**", new String[]{"classpath:/app/dist/assets/"});
  public static final Map<String, String> VIEW_CONTROLLER_2_VIEW_NAME = Map.of("/app/**", "index.html");

  public static void main(String[] args) {
    var app = new SpringApplication(Main.class);
    app.setBannerMode(Mode.OFF);
    app.run(args);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    RESOURCE_HANDLER_2_RESOURCE_LOCATIONS.forEach((resourceHandler, resourceLocations) -> {
      registry.addResourceHandler(resourceHandler).addResourceLocations(resourceLocations);
    });
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    VIEW_CONTROLLER_2_VIEW_NAME.forEach((viewController, viewName) -> {
      registry.addViewController(viewController).setViewName(viewName);
    });
  }
}
