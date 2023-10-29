package org.shjwfan.security.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum FreelancersHubAuthority implements GrantedAuthority {
  ADMIN("admin");

  @Getter
  private final String authority;
}
