package org.shjwfan.security.core.userdetails;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ConfigurationProperties("freelancers-hub")
@Configuration
public class ConfigurationPropertiesUserDetailsService implements FreelancersHubUserDetailsService {

  private @Autowired PasswordEncoder passwordEncoder;
  private Map<String, ConfigurationPropertiesUserDetails> email2User = Map.of();
  private Map<String, ConfigurationPropertiesUserDetails> username2User = Map.of();

  @Override
  public FreelancersHubUserDetails loadUserByEmail(String email) throws EmailNotFoundException {
    FreelancersHubUserDetails user = email2User.get(email);
    if (user == null) {
      throw new EmailNotFoundException(email);
    }
    return user;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    FreelancersHubUserDetails user = username2User.get(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return user;
  }

  public void setUsers(ConfigurationPropertiesUserDetails... users) {
    Stream.of(users).forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
    this.email2User = Stream.of(users).collect(Collectors.toUnmodifiableMap(ConfigurationPropertiesUserDetails::getEmail, user -> user));
    this.username2User = Stream.of(users).collect(Collectors.toUnmodifiableMap(ConfigurationPropertiesUserDetails::getUsername, user -> user));
  }
}
