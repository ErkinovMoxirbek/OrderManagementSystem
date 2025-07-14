package com.example.service;

import com.example.entity.OrderEntity;
import com.example.entity.OrderItemEntity;
import com.example.entity.ProductEntity;
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
        ProductEntity productEntity = productRepository.findById(entity.getId()).get();
        OrderEntity orderEntity = orderRepository.findById(entity.getId()).get();
        entity.setTotalPrice(productEntity.getPrice() * entity.getQuantity());
        entity.setUnitPrice(productEntity.getPrice());
        productEntity.setStock(productEntity.getStock() - entity.getQuantity());
        orderEntity.setTotalAmount(productEntity.getPrice() * entity.getQuantity());
         return orderItemRepository.save(entity);
    }
    public List<OrderItemEntity> getAll() {
        return orderItemRepository.findAll();
    }
}
