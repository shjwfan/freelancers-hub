package org.shjwfan.security.core.userdetails;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.shjwfan.security.core.FreelancersHubAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class ConfigurationPropertiesUserDetails implements UserDetails {

  private Set<? extends GrantedAuthority> authorities;
  private String password;
  private String username;
  private Instant accountExpiredAt;
  private boolean accountLocked = false;
  private Instant credentialsExpiredAt;
  private boolean enabled = false;

  public void setAuthorities(String authorities) {
    String[] values = authorities.split(",");
    this.authorities = Stream.of(values).map(value -> FreelancersHubAuthority.valueOf(value)).collect(Collectors.toSet());
  }

  @Override
  public boolean isAccountNonExpired() {
    Instant now = Instant.now();
    return accountExpiredAt.isAfter(now);
  }

  @Override
  public boolean isAccountNonLocked() {
    return !accountLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    Instant now = Instant.now();
    return credentialsExpiredAt.isAfter(now);
  }

  @Override
  public boolean isEnabled() {
    return enabled;
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
