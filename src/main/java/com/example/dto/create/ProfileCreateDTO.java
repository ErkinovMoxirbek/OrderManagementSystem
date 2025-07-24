package com.example.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProfileCreateDTO {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 6, max = 20)
    private String password;
}
