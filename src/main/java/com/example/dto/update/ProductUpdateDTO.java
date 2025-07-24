package com.example.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductUpdateDTO {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 0)
    private Double price;
    @NotNull
    private Integer stock;
}
