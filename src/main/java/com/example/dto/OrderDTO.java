package com.example.dto;

import com.example.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private Long id;
    @NotBlank
    private String customerName;
    @NotBlank
    @Email
    private String customerEmail;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Double totalAmount;

}
