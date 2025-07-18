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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    public OrderDTO addOrder(OrderDTO orderDTO) {
        String email = orderDTO.getCustomerEmail();
        if (email != null && !isValidEmail(email)) {
            logger.warn("Email is not valid {}", email);
            throw new BadRequestException("Email format is incorrect.: " + email);
        }
        if (orderRepository.findByCustomerEmail(email) != null && orderRepository.findAllByCustomerEmail(email).getOrderStatus().equals(OrderStatus.PENDING)) {
            logger.debug("Order is already exists {}", email);
            throw new OrderAlreadyExistsException(email);
        }
        if (orderDTO.getCustomerName().length() < 3) {
            logger.warn("Customer name is too short {}", orderDTO.getCustomerName());
            throw new BadRequestException("The customer's name is not valid.");
        }
        OrderEntity orderEntity = orderRepository.save(toEntity(orderDTO));
        orderDTO.setId(orderEntity.getId());
        orderDTO.setOrderStatus(orderEntity.getOrderStatus());
        orderDTO.setOrderDate(orderEntity.getOrderDate());
        orderDTO.setTotalAmount(orderEntity.getTotalAmount());
        logger.info("Order added: {}", orderDTO.toString());
        return orderDTO;
    }


    public List<OrderDTO> getAllOrders() {
        logger.info("Get all orders");
        return orderRepository.findAllDTO();
    }

    public OrderDTO getOrderId(Long id) {
        logger.info("Get order id {}", id);
        return orderRepository.findByIdDTO(id);
    }

    public OrderDTO updateService(Long id, OrderStatus orderStatus) {
        if (!orderRepository.existsById(id)) {
            logger.warn("Order not found {}", id);
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
        logger.info("Order updated {}", orderEntity.toString());
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
        logger.info("Reverting stocks");
    }

    public Boolean deleteOrder(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElse(null);
        if (orderEntity != null) {
            logger.info("Order deleted {}", orderEntity.toString());
            orderRepository.delete(orderEntity);
            return true;
        }
        return false;
    }

    public OrderDTO getOrdersByEmail(String email) {
        if (!isValidEmail(email)) {
            logger.warn("Email is not valid {}", email);
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
        return orderEntity;
    }

    //Orderni tekshirish agar pending bolsa orderItemsni o'zgartirsa boladi boshqa statusda yo'q
    public void checkStatus(OrderDTO orderDTO) {
        if (orderDTO.getOrderStatus() != OrderStatus.PENDING) {
            logger.warn("Order is not PENDING {}", orderDTO.getOrderStatus());
            throw new BadRequestException("Order not edited!");
        }
    }
}
