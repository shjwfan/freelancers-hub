package org.shjwfan.security.core.userdetails;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface EmailBasedUserDetailsService extends UserDetailsService {

  EmailBasedUserDetails loadUserByEmail(String email) throws EmailNotFoundException;
}
