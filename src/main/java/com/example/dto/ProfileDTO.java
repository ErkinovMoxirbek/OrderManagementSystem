package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.*;

@Getter
@Setter
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private ProfileRole role;
}
