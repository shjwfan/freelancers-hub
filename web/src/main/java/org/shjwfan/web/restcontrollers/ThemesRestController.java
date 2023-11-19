package org.shjwfan.web.restcontrollers;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThemesRestController {

  @GetMapping("/api/v1/themes")
  public Set<ThemeDto> loadThemes() {
    return Set.of("3D Astronaut", "Space Fantastic Vintage Poster",
                  "Startup Rocket Retro Logo").stream()
        .map(ThemeDto::new)
        .collect(Collectors.toUnmodifiableSet());
  }

  public record ThemeDto(String name) {

  }
}
