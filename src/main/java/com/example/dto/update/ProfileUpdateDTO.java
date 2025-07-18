package com.example.dto.update;

import com.example.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateDTO {
    private String id;
    private String fullName;
    private String email;
    private String password;
    private ProfileRole role;
}
