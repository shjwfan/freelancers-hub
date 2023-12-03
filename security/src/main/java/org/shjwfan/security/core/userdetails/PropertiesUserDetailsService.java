package org.shjwfan.security.core.userdetails;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;
import org.shjwfan.security.crypto.password.PasswordReseter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ConfigurationProperties("freelancers-hub")
@Configuration
public class PropertiesUserDetailsService implements EmailBasedUserDetailsService, PasswordReseter {

  private @Autowired PasswordEncoder passwordEncoder;
  private Map<String, PropertiesUserDetails> email2User = Map.of();
  private Map<String, PropertiesUserDetails> username2User = Map.of();

  @Override
  public EmailBasedUserDetails loadUserByEmail(String email) throws EmailNotFoundException {
    EmailBasedUserDetails user = email2User.get(email);
    if (user == null) {
      throw new EmailNotFoundException(email);
    }
    return user;
  }

  @Override
  public EmailBasedUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    EmailBasedUserDetails user = username2User.get(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return user;
  }

  @Override
  public void reset(String subject, String actualPassword) {
    var user = ObjectUtils.firstNonNull(email2User.get(subject), username2User.get(subject));
    if (user == null) {
      throw new IllegalStateException(); // business exception?
    }
    user.setPassword(actualPassword);
  }

  public void setUsers(PropertiesUserDetails... users) {
    Stream.of(users).forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
    this.email2User = Stream.of(users).collect(Collectors.toUnmodifiableMap(PropertiesUserDetails::getEmail, user -> user));
    this.username2User = Stream.of(users).collect(Collectors.toUnmodifiableMap(PropertiesUserDetails::getUsername, user -> user));
  }
}
