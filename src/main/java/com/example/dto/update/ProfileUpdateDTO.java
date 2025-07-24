package com.example.dto.update;

import com.example.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProfileUpdateDTO {
    @NotNull @NotEmpty @Size(min = 3, max = 50)
    private String fullName;
}
