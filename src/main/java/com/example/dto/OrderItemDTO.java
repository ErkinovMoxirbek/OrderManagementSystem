package com.example.dto;

import com.example.entity.OrderEntity;
import com.example.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDTO {
    private Long id;
    private OrderEntity order;
    private ProductEntity product;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
