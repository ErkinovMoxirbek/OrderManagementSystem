package com.example.dto.create;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class OrderCreateDTO {
    @NotNull
    @Email
    private String email;
}
