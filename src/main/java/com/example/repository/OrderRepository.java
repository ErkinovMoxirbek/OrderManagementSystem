package com.example.repository;

import com.example.dto.OrderDTO;
import com.example.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {


    @Query("""
        SELECT new com.example.dto.OrderDTO(
            o.id,
            o.customerName,
            o.customerEmail,
            o.orderDate,
            o.orderStatus,
            o.totalAmount
        )
        FROM OrderEntity o
    """)

    List<OrderDTO> findAllDTO();

    @Query("""
        SELECT new com.example.dto.OrderDTO(
            o.id,
            o.customerName,
            o.customerEmail,
            o.orderDate,
            o.orderStatus,
            o.totalAmount
        )
        FROM OrderEntity o
        where o.customerEmail = :email
    """)
    List<OrderDTO> findAllByCustomerEmail(String email);
   @Query("""
        SELECT new com.example.dto.OrderDTO(
            o.id,
            o.customerName,
            o.customerEmail,
            o.orderDate,
            o.orderStatus,
            o.totalAmount
        )
        FROM OrderEntity o
        WHERE o.id = :id
    """)
    OrderDTO findByIdDTO(Long id);
}
