package com.example.service;

import com.example.dto.OrderDTO;
import com.example.entity.OrderEntity;
import com.example.entity.ProductEntity;
import com.example.exception.BadRequestException;
import com.example.exception.OrderAlreadyExistsException;
import com.example.repository.OrderRepository;
import com.example.enums.OrderStatus;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public OrderDTO addOrder(OrderDTO orderDTO) {
        String email = orderDTO.getCustomerEmail();
        if (!isValidEmail(email)) {
            throw new BadRequestException("Email format is incorrect.: " + email);
        }
        if (orderRepository.findByCustomerEmail(email) != null) {
            throw new OrderAlreadyExistsException(email);
        }
        OrderEntity orderEntity = orderRepository.save(toEntity(orderDTO));
        orderDTO.setId(orderEntity.getId());
        return orderDTO;
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
            orderRepository.delete(orderEntity);
            return true;
       }
       return false;
    }

    public List<OrderDTO> getOrdersByEmail( String email) {
        if (!isValidEmail(email)) {
            throw new BadRequestException("Email format is incorrect.: " + email);
        }
        return orderRepository.findAllByCustomerEmail(email);
    }
    public boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
    public OrderEntity toEntity(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerName(orderDTO.getCustomerName());
        orderEntity.setCustomerEmail(orderDTO.getCustomerEmail());
        orderEntity.setTotalAmount(orderDTO.getTotalAmount());
        return orderEntity;
    }
}
