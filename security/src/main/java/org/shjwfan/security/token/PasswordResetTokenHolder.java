package org.shjwfan.security.token;

import jakarta.annotation.Nullable;
import java.util.Optional;

public interface PasswordResetTokenHolder {

  Optional<String> getPasswordResetToken(@Nullable String subject);

  void putPasswordResetToken(String subject, String passwordResetToken);

  Optional<String> removePasswordResetToken(@Nullable String subject);
}
