package com.example.service;

import com.example.dto.OrderDTO;
import com.example.dto.create.OrderCreateDTO;
import com.example.entity.OrderEntity;
import com.example.entity.OrderItemEntity;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.exception.BadRequestException;
import com.example.exception.OrderNotFoundException;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.enums.OrderStatus;
import com.example.repository.ProductRepository;
import com.example.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    public OrderDTO addOrder(OrderCreateDTO orderCreateDTO) {
        Optional<ProfileEntity> profileEntity = profileRepository.findByEmailAndVisibleTrue(orderCreateDTO.getEmail());
        if (profileEntity.isEmpty()) {
            logger.error("Profile not found");
            throw new BadRequestException("Profile not found");
        }
        OrderEntity orderEntity = orderRepository.save(toEntity(orderCreateDTO));
        System.out.println(orderEntity);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderEntity.getId());
        orderDTO.setOrderStatus(orderEntity.getOrderStatus());
        orderDTO.setOrderDate(orderEntity.getOrderDate());
        orderDTO.setTotalAmount(orderEntity.getTotalAmount());
        orderDTO.setEmail(orderEntity.getProfileEntity().getEmail());
        logger.info("Order added: {}", orderDTO);
        return orderDTO;
    }


    public List<OrderDTO> getAllOrders() {
        logger.info("Get all orders");
        return orderRepository.findAllDTO();
    }

    public OrderDTO getOrderId(Long id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if (orderEntity.isEmpty()) {
            logger.error("Order not found");
            throw new OrderNotFoundException(id);
        }
        logger.info("Get order id {}", id);
        return orderRepository.findByIdDTO(id);
    }

    public OrderDTO updateOrder(Long id, OrderStatus orderStatus) {
        if (!orderRepository.existsById(id)) {
            logger.warn("Order not found {}", id);
            throw new OrderNotFoundException(id);
        }
        OrderEntity orderEntity = orderRepository.findById(id).get();
        orderEntity.setOrderStatus(orderStatus);
        if (OrderStatus.CANCELLED.equals(orderStatus)) {
            ProductEntity productEntity = productRepository.findById(id).get();
            revertingStocks(productRepository.findAll(),orderItemRepository.findAllByOrderId(orderEntity.getId()));
            orderEntity.setOrderStatus(OrderStatus.CANCELLED);
            productRepository.save(productEntity);
            orderRepository.save(orderEntity);
        }
        logger.info("Order updated {}", orderEntity);
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
            logger.info("Order deleted {}", orderEntity);
            orderRepository.delete(orderEntity);
            return true;
        }
        return false;
    }

    public OrderDTO getOrdersByEmail(String email) {
        if (profileService.isValidEmail(email)) {
            logger.warn("Email is not valid {}", email);
            throw new BadRequestException("Email format is incorrect.: " + email);
        }
        return orderRepository.findAllByCustomerEmail(email);
    }



    public OrderEntity toEntity(OrderCreateDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setProfileEntity(profileRepository.findByEmailAndVisibleTrue(orderDTO.getEmail()).get());
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
