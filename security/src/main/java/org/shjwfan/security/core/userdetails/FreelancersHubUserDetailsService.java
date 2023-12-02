package org.shjwfan.security.core.userdetails;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface FreelancersHubUserDetailsService extends UserDetailsService {

  FreelancersHubUserDetails loadUserByEmail(String email) throws EmailNotFoundException;
}
