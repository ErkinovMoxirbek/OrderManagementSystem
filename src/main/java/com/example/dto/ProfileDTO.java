package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private String id;
    @NotNull
    @NotBlank
    private String fullName;
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    private String password;
    private ProfileRole role;
}
