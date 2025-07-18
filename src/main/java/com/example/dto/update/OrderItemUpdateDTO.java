package com.example.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemUpdateDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
