package org.shjwfan.security.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooBarController {

  @GetMapping("/api/v1/foo")
  public String foo() {
    return "foo";
  }

  @GetMapping("/api/v1/bar")
  public String bar() {
    return "bar";
  }
}
