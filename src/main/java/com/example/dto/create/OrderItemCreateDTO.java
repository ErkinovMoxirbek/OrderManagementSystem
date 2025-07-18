package com.example.dto.create;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderItemCreateDTO {
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
