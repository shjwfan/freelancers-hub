package org.shjwfan.web.restcontrollers.login;

import jakarta.annotation.Nullable;

public record LoginRequestBody(@Nullable String username, @Nullable String password) {

}
