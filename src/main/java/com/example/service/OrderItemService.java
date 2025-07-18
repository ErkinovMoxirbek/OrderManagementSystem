package com.example.service;

import com.example.dto.OrderItemDTO;
import com.example.dto.create.OrderItemCreateDTO;
import com.example.entity.OrderEntity;
import com.example.entity.OrderItemEntity;
import com.example.entity.ProductEntity;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
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

        public OrderItemDTO add(@Valid OrderItemCreateDTO orderItemCreateDTO) {

            if (orderItemCreateDTO.getQuantity() == null || orderItemCreateDTO.getQuantity() <= 0) {
                logger.warn("Quantity must be greater than 0 your quantity: {}" , orderItemCreateDTO.getQuantity());
                throw new BadRequestException("Quantity must be greater than 0 your quantity: " + orderItemCreateDTO.getQuantity());
            }


            //creatDTOdan malumotlarni dtogakochirish
            OrderItemDTO dto = new OrderItemDTO();
            dto.setOrderId(orderItemCreateDTO.getOrderId());
            dto.setProductId(orderItemCreateDTO.getProductId());
            dto.setQuantity(orderItemCreateDTO.getQuantity());

            //valid product and order
            if (orderItemCreateDTO.getProductId() == null || orderItemCreateDTO.getOrderId() == null) {
                logger.warn("Order or Product not found is Order id: {}", orderItemCreateDTO.getOrderId()+ ", Product id: " + orderItemCreateDTO.getProductId());
                throw new BadRequestException("Order or Product Bad request --> Order id: " + orderItemCreateDTO.getOrderId()+ ", Product id: " + orderItemCreateDTO.getProductId());
            }
            ProductEntity productEntity = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> {
                        logger.error("Product not found: {}", dto.getProductId());
                        return new ProductNotFoundException(dto.getProductId());
                    });
            if (!productEntity.getIsActive() ||
                    productEntity.getStock() < dto.getQuantity()){
                logger.warn("We do not have the quantity or product in stock: {}" , dto.getQuantity());
                throw new BadRequestException("We do not have the quantity or product in stock: " + dto.getQuantity());

            }

            OrderEntity orderEntity = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> {
                        logger.error("Order not found: {}", dto.getOrderId());
                        return new OrderNotFoundException(dto.getOrderId());
                    });


            orderService.checkStatus(orderRepository.findByIdDTO(dto.getOrderId()));




            OrderItemEntity orderItemEntity = orderItemRepository.findByOrderIdAndProductId(dto.getOrderId(), dto.getProductId());

            logger.debug("OrderItem lookup: orderId={}, productId={}, found={}", dto.getOrderId(), dto.getProductId(), orderItemEntity != null);

            //Order Item yoq bolsa
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
                orderEntity.setTotalAmount(orderEntity.getTotalAmount() + orderItemEntity.getTotalPrice());

            }else {
                logger.info("Updating existing OrderItem for orderId={}, productId={}", dto.getOrderId(), dto.getProductId());
                orderItemEntity.setQuantity(orderItemEntity.getQuantity() + dto.getQuantity());
                orderItemEntity.setUnitPrice(productEntity.getPrice());
                productEntity.setStock(productEntity.getStock() - dto.getQuantity());
                orderEntity.setTotalAmount(productEntity.getPrice() * orderItemEntity.getQuantity());
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



}
