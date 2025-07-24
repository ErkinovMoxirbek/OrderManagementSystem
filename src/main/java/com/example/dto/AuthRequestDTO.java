package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthRequestDTO {
    @Email
    private String email;
    @NotEmpty @NotNull @Size(min = 6)
    private String password;
}