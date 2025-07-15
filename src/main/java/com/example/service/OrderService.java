package com.example.service;

import com.example.dto.OrderDTO;
import com.example.entity.OrderEntity;
import com.example.entity.OrderItemEntity;
import com.example.entity.ProductEntity;
import com.example.exception.BadRequestException;
import com.example.exception.OrderAlreadyExistsException;
import com.example.exception.OrderNotFoundException;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.enums.OrderStatus;
import com.example.repository.ProductRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderDTO addOrder(OrderDTO orderDTO) {
        String email = orderDTO.getCustomerEmail();
        if (!isValidEmail(email)) {
            System.out.println("Email is not valid");
            throw new BadRequestException("Email format is incorrect.: " + email);
        }
        if (orderRepository.findByCustomerEmail(email) != null && orderRepository.findAllByCustomerEmail(email).getOrderStatus().equals(OrderStatus.PENDING)) {
            System.out.println("Order already exists");
            throw new OrderAlreadyExistsException(email);
        }
        if (orderDTO.getCustomerName().length() < 3) {
            System.out.println("Customer name is not too long");
            throw new BadRequestException("The customer's name is not valid.");
        }
        OrderEntity orderEntity = orderRepository.save(toEntity(orderDTO));
        orderDTO.setId(orderEntity.getId());
        orderDTO.setOrderStatus(orderEntity.getOrderStatus());
        orderDTO.setOrderDate(orderEntity.getOrderDate());
        return orderDTO;
    }


    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllDTO();
    }

    public OrderDTO getOrderId(Long id) {
        return orderRepository.findByIdDTO(id);
    }

    public OrderDTO updateService(Long id, OrderStatus orderStatus) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        OrderEntity orderEntity = orderRepository.findById(id).get();
        orderEntity.setOrderStatus(orderStatus);
        if (OrderStatus.CANCELLED.equals(orderStatus)) {
            ProductEntity productEntity = productRepository.findById(id).get();
            OrderItemEntity orderItemEntity = orderItemRepository.findById(id).get();
            revertingStocks(productRepository.findAll(),orderItemRepository.findAllByOrderId(orderEntity.getId()));
            orderEntity.setOrderStatus(OrderStatus.CANCELLED);
            productRepository.save(productEntity);
            orderRepository.save(orderEntity);
        }
        return orderRepository.findByIdDTO(id);
    }
    public void revertingStocks (List<ProductEntity> productEntities , List<OrderItemEntity> orderItemEntities){
        for (OrderItemEntity o : orderItemEntities) {
            for (ProductEntity e : productEntities) {
                if (o.getProductId().equals(e.getId())) {
                    e.setStock(e.getStock() + o.getQuantity());
                    if (e.getStock() > 0)e.setIsActive(true);
                    productRepository.save(e);
                }
            }
        }
    }

    public Boolean deleteOrder(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElse(null);
        if (orderEntity != null) {
            orderRepository.delete(orderEntity);
            return true;
        }
        return false;
    }

    public OrderDTO getOrdersByEmail(String email) {
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

    //Orderni tekshirish agar pending bolsa orderItemsni o'zgartirsa boladi boshqa statusda yo'q
    public void checkStatus(OrderDTO orderDTO) {
        if (orderDTO.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order not edited!");
        }
    }
}
