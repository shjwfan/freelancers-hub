package org.shjwfan.security.core.userdetails;

import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ConfigurationProperties("freelancers-hub")
@Configuration
public class ConfigurationPropertiesUserDetailsService implements UserDetailsService {

  private @Autowired PasswordEncoder passwordEncoder;
  private Set<ConfigurationPropertiesUserDetails> users = Collections.emptySet();

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return users.stream().filter(user -> StringUtils.equals(user.getUsername(), username)).findAny().orElseThrow(() -> new UsernameNotFoundException(username));
  }

  public void setUsers(Set<ConfigurationPropertiesUserDetails> users) {
    users.forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
    this.users = users;
  }
}
