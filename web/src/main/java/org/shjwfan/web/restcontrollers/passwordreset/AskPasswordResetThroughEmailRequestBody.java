package org.shjwfan.web.restcontrollers.passwordreset;

import jakarta.annotation.Nullable;

public record AskPasswordResetThroughEmailRequestBody(@Nullable String email, @Nullable String actualPassword,
                                                      @Nullable String confirmRedirect, @Nullable String discardRedirect) {

}
