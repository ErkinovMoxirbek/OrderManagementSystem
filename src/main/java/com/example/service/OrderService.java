package com.example.service;

import com.example.dto.OrderDTO;
import com.example.entity.OrderEntity;
import com.example.repository.OrderRepository;
import com.example.status.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public OrderEntity addOrder(OrderEntity orderEntity) {
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllDTO();
    }

    public OrderDTO getOrderId(Long id) {
        return orderRepository.findByIdDTO(id);
    }

    public OrderDTO updateService(Long id,OrderStatus orderStatus) {
            OrderEntity orderEntity = orderRepository.findById(id).get();
            orderEntity.setOrderStatus(orderStatus);
            orderRepository.save(orderEntity);
            return orderRepository.findByIdDTO(id);
    }

    public Boolean deleteOrder(Long id) {
       OrderEntity orderEntity = orderRepository.findById(id).orElse(null);
       if(orderEntity != null) {
           orderEntity.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(orderEntity);
            return true;
       }
       return false;
    }

    public List<OrderDTO> getOrdersByEmail(String email) {
        return orderRepository.findAllByCustomerEmail(email);
    }
}
