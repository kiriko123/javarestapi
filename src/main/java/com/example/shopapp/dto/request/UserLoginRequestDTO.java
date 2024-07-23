package com.example.shopapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class UserLoginRequestDTO implements Serializable {
    @JsonProperty("phone_number")
    @NotBlank
    private String phoneNumber;
    @JsonProperty("password")
    @NotBlank
    private String password;
}
