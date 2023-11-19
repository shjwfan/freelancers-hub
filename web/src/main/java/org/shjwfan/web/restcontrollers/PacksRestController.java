package org.shjwfan.web.restcontrollers;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacksRestController {

  @GetMapping("/api/v1/packs")
  public Set<PackDto> loadPacks() {
    return Set.of("All", "Illustration", "Animation", "3D Astronaut", "Font").stream()
        .map(PackDto::new).collect(Collectors.toUnmodifiableSet());
  }

  public record PackDto(String name) {

  }
}
