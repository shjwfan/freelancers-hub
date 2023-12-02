package org.shjwfan.security.core.userdetails;

import org.springframework.security.core.userdetails.UserDetails;

public interface FreelancersHubUserDetails extends UserDetails {

  String getEmail();
}
