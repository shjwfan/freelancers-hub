package org.shjwfan.security.core;

import org.springframework.security.core.GrantedAuthority;

public enum FreelancersHubAuthority implements GrantedAuthority {
  ADMIN("admin");

  private final String authority;

  FreelancersHubAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }
}
