package com.example.repository;

import com.example.dto.OrderDTO;
import com.example.dto.OrderItemDTO;
import com.example.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
   OrderItemEntity findByProductId(long productId);
   OrderItemEntity findByOrderId(long orderId);
   OrderItemEntity findByOrderIdAndProductId(long orderId, long productId);
   List<OrderItemEntity> findAllByOrderId(long orderId);
    @Query("""
        SELECT new com.example.dto.OrderItemDTO(
            o.id,
            o.orderId,
            o.productId,
            o.quantity,
            o.unitPrice,
            o.totalPrice
        )
        FROM OrderItemEntity o
    """)
    List<OrderItemDTO> findAllDTO();

}
