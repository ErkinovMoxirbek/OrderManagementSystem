package com.example.dto.create;

import lombok.Data;

@Data
public class ProductCreateDTO {
    private String name;
    private Double price;
    private Integer stock;
    private String category;
}
