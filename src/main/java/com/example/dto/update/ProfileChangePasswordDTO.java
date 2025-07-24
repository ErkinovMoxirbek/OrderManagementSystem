package com.example.dto.update;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProfileChangePasswordDTO {
    @NotNull @Size(min = 6 ,max = 50)
    private String password;
}
