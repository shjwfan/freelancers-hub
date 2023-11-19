package org.shjwfan.web.restcontrollers;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorksRestController {

  @GetMapping("/api/v1/works")
  public Set<WorkDto> loadWorks() {
    return Set.of("3D_Astronaut.jpg", "Space_Fantastic_Vintage_Poster.jpg",
                  "Startup_Rocket_Retro_Logo.jpg").stream()
        .map(WorkDto::new)
        .collect(Collectors.toUnmodifiableSet());
  }

  public record WorkDto(String name) {

  }
}
