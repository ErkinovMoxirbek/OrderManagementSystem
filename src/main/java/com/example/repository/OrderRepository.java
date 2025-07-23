package com.example.repository;

import com.example.dto.OrderDTO;
import com.example.entity.OrderEntity;
import com.example.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByProfileEntityAndVisibleTrue(ProfileEntity profileEntity);

    @Query("""
        SELECT new com.example.dto.OrderDTO(
            o.id,
            o.profileEntity.email,
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
            o.profileEntity.email,
            o.orderDate,
            o.orderStatus,
            o.totalAmount
        )
        FROM OrderEntity o
        where o.profileEntity.email = :email
    """)
    OrderDTO findAllByCustomerEmail(String email);
   @Query("""
        SELECT new com.example.dto.OrderDTO(
            o.id,
            o.profileEntity.email,
            o.orderDate,
            o.orderStatus,
            o.totalAmount
        )
        FROM OrderEntity o
        WHERE o.id = :id
    """)
    OrderDTO findByIdDTO(Long id);
}
