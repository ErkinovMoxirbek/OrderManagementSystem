package com.example.dto;

import com.example.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class OrderDTO {
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Double totalAmount;

}
