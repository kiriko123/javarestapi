package com.example.shopapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserRequestDTO implements Serializable {
    @JsonProperty("fullname")
    private String fullName;
    @NotBlank
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    @NotBlank
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @NotNull
    @JsonProperty("role_id")
    private long roleId;
}
