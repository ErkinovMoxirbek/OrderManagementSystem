package com.example.service;

import com.example.dto.OrderItemDTO;
import com.example.entity.OrderEntity;
import com.example.entity.OrderItemEntity;
import com.example.entity.ProductEntity;
import com.example.exception.BadRequestException;
import com.example.exception.OrderNotFoundException;
import com.example.exception.ProductNotFoundException;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private OrderService orderService;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

        public OrderItemDTO add(@Valid OrderItemDTO dto) {
            logger.info("OrderItemDTO: {}", dto);
            orderService.checkStatus(orderRepository.findByIdDTO(dto.getOrderId()));
            if (dto.getQuantity() < 1 ||
                    !productRepository.findById(dto.getProductId()).get().getIsActive() ||
                    productRepository.findById(dto.getProductId()).get().getStock() < dto.getQuantity() ) {
                logger.warn("Product unsuitable: {}", dto);
                throw new BadRequestException("Product unsuitable!");
            }

            ProductEntity productEntity = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> {
                        logger.error("Product not found: {}", dto.getProductId());
                        return new ProductNotFoundException(dto.getProductId());
                    });

            OrderEntity orderEntity = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> {
                        logger.error("Order not found: {}", dto.getOrderId());
                        return new OrderNotFoundException(dto.getOrderId());
                    });
            //Order Item yoq bolsa
            OrderItemEntity orderItemEntity = orderItemRepository.findByOrderIdAndProductId(dto.getOrderId(), dto.getProductId());
            logger.debug("OrderItem lookup: orderId={}, productId={}, found={}", dto.getOrderId(), dto.getProductId(), orderItemEntity != null);

            if (orderItemEntity == null) {
                logger.info("Creating new OrderItem for orderId={}, productId={}", dto.getOrderId(), dto.getProductId());
                orderItemEntity = new OrderItemEntity();
                // narxlarni hisoblash
                orderItemEntity.setUnitPrice(productEntity.getPrice());
                orderItemEntity.setTotalPrice(productEntity.getPrice() * dto.getQuantity());
                orderItemEntity.setProductId(dto.getProductId());
                orderItemEntity.setOrderId(dto.getOrderId());
                orderItemEntity.setQuantity(dto.getQuantity());
                // product stockni kamaytirish
                productEntity.setStock(productEntity.getStock() - dto.getQuantity());
                productRepository.save(productEntity);

                // order total amountni oshirish
                System.out.println(orderEntity);
                orderEntity.setTotalAmount(orderEntity.getTotalAmount() + orderItemEntity.getTotalPrice());

            }else {
                logger.info("Updating existing OrderItem for orderId={}, productId={}", dto.getOrderId(), dto.getProductId());
                orderItemEntity.setQuantity(orderItemEntity.getQuantity() + dto.getQuantity());
                orderItemEntity.setUnitPrice(productEntity.getPrice());
                productEntity.setStock(productEntity.getStock() - dto.getQuantity());
                orderEntity.setTotalAmount(orderItemEntity.getUnitPrice() * orderItemEntity.getQuantity());
                orderItemEntity.setTotalPrice(orderEntity.getTotalAmount());
            }

            dto.setUnitPrice(orderItemEntity.getUnitPrice());
            dto.setTotalPrice(orderItemEntity.getTotalPrice());
            dto.setQuantity(orderItemEntity.getQuantity());

            orderRepository.save(orderEntity);
            orderItemRepository.save(orderItemEntity);
            dto.setId(orderEntity.getId());
            if (productEntity.getStock() == 0) {
                logger.info("Product stock is now zero; deactivating productId={}", productEntity.getId());
                productEntity.setIsActive(false);
            }
            productRepository.save(productEntity);
            logger.info("OrderItem successfully added: {}", dto);
            return dto;
        }

    public List<OrderItemDTO> getAll() {
        return orderItemRepository.findAllDTO();
    }


    private OrderItemEntity toEntity(@Valid OrderItemDTO dto) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderId(dto.getOrderId());
        orderItemEntity.setProductId(dto.getProductId());
        orderItemEntity.setQuantity(dto.getQuantity());
        orderItemEntity.setUnitPrice(dto.getUnitPrice());
        orderItemEntity.setTotalPrice(dto.getTotalPrice());
        return orderItemEntity;
    }
}
