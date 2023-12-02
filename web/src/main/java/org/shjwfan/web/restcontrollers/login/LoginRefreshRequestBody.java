package org.shjwfan.web.restcontrollers.login;

import jakarta.annotation.Nullable;

public record LoginRefreshRequestBody(@Nullable String currentRefreshToken) {

}
