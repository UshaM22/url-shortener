package com.urlshortener.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRegistrationRequest {

    @Email @NotBlank
    private String userName;

    @Size(min=8) @NotBlank
    private String password;
}
