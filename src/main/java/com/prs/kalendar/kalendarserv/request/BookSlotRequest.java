package com.prs.kalendar.kalendarserv.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prs.kalendar.kalendarserv.util.CommonUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

public class BookSlotRequest {
    @NotBlank
    @Pattern(regexp = CommonUtils.EMAIL_REGEX)
    @JsonProperty("email_id")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
