package com.example.dto.create;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProductCreateDTO {
    @NotBlank
    @NotNull
    private String name;
    @NotNull @Size(min = 0, max = 999999999)
    private Double price;
    @NotNull @Size(min = 1, max = 9999)
    private Integer stock;
    @NotNull @NotBlank
    private String category;
}
