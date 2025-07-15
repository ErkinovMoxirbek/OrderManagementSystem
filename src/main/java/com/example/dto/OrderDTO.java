package com.example.dto;

import com.example.enums.OrderStatus;

import java.time.LocalDateTime;

public class OrderDTO {
    private Long id;
    private String customerName;
    private String customerEmail;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Double totalAmount;

    public OrderDTO(Long id, String customerName, String customerEmail, LocalDateTime orderDate, OrderStatus orderStatus, Double totalAmount) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
    }

    public OrderDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
