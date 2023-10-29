package org.shjwfan.security.core.userdetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.shjwfan.security.core.FreelancersHubAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ConfigurationPropertiesUserDetails implements UserDetails {

  private Set<? extends GrantedAuthority> authorities = Collections.emptySet();
  private String password = "";
  private String username = "";
  private Instant accountExpiredAt = Instant.MAX;
  private boolean accountLocked = false;
  private Instant credentialsExpiredAt = Instant.MAX;
  private boolean enabled = false;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(String authorities) {
    String[] values = authorities.split(",");
    this.authorities = Stream.of(values).map(value -> FreelancersHubAuthority.valueOf(value)).collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean isAccountNonExpired() {
    Instant now = Instant.now();
    return accountExpiredAt.isAfter(now);
  }

  public void setAccountExpiredAt(String accountExpiredAt) {
    this.accountExpiredAt = Instant.parse(accountExpiredAt);
  }

  @Override
  public boolean isAccountNonLocked() {
    return !accountLocked;
  }

  public void setAccountLocked(boolean accountLocked) {
    this.accountLocked = accountLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    Instant now = Instant.now();
    return credentialsExpiredAt.isAfter(now);
  }

  public void setCredentialsExpiredAt(String credentialsExpiredAt) {
    this.credentialsExpiredAt = Instant.parse(credentialsExpiredAt);
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurationPropertiesUserDetails user = (ConfigurationPropertiesUserDetails) o;
    return new EqualsBuilder().append(username, user.username).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(username).toHashCode();
  }
}
