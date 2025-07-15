package com.example.service;

import com.example.dto.OrderItemDTO;
import com.example.entity.OrderEntity;
import com.example.entity.OrderItemEntity;
import com.example.entity.ProductEntity;
import com.example.exception.OrderNotFoundException;
import com.example.exception.ProductNotFoundException;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    public OrderItemEntity add(@Valid OrderItemEntity entity) {
        // productId va orderId bo‘yicha topish
        ProductEntity productEntity = productRepository.findById(entity.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(entity.getProductId()));

        OrderEntity orderEntity = orderRepository.findById(entity.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(entity.getOrderId()));

        // narxlarni hisoblash
        entity.setUnitPrice(productEntity.getPrice());
        entity.setTotalPrice(productEntity.getPrice() * entity.getQuantity());

        // product stockni kamaytirish
        productEntity.setStock(productEntity.getStock() - entity.getQuantity());
        productRepository.save(productEntity);

        // order total amountni oshirish
        orderEntity.setTotalAmount(orderEntity.getTotalAmount() + entity.getTotalPrice());
        orderRepository.save(orderEntity);

        // order itemni saqlash
        return orderItemRepository.save(entity);
    }

    public List<OrderItemDTO> getAll() {
        return orderItemRepository.findAllDTO();
    }
}
