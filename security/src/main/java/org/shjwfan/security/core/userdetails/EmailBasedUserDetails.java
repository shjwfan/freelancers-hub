package org.shjwfan.security.core.userdetails;

import org.springframework.security.core.userdetails.UserDetails;

public interface EmailBasedUserDetails extends UserDetails {

  String getEmail();
}
