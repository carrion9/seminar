package com.vetx.starter.payload.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank
    @Size(min = 4, max = 100)
    private String name;

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
