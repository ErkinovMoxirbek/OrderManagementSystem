package com.example.service;

import com.example.dto.ProductDTO;
import com.example.dto.create.ProductCreateDTO;
import com.example.entity.ProductEntity;
import com.example.exception.BadRequestException;
import com.example.exception.InsufficientStockException;
import com.example.exception.NotFoundException;
import com.example.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    public ProductDTO add(ProductCreateDTO productCreateDTO) {
        if (productCreateDTO.getStock() == null || productCreateDTO.getStock() <= 0) {
            logger.warn("Stock should be greater than 0");
            throw new InsufficientStockException("Stock should be greater than 0");
        }
        if (productCreateDTO.getName() == null || productCreateDTO.getName().trim().isEmpty()) {
            logger.error("Product name is empty");
            throw new BadRequestException("Product name cannot be empty");
        }
        if (productCreateDTO.getCategory() == null || productCreateDTO.getCategory().trim().isEmpty()) {
            logger.error("Product category is empty");
            throw new BadRequestException("Product category cannot be empty");
        }
        if (productCreateDTO.getPrice() == null || productCreateDTO.getPrice() < 0){
            logger.error("Product price is short zero : {}" , productCreateDTO.getPrice());
            throw new BadRequestException("Product price cannot be less than zero");
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productCreateDTO.getName());
        productEntity.setPrice(productCreateDTO.getPrice());
        productEntity.setStock(productCreateDTO.getStock());
        productEntity.setCategory(productCreateDTO.getCategory());
        ProductEntity productEntity1 = productRepository.findByName(productEntity.getName());
        //agar mahsulot bor bolsa qoshmay kopaytirib qoyadi
         if (productEntity1.getStock() > 0) {
            productEntity1.setStock(productEntity.getStock() + productEntity1.getStock());
            productEntity1.setPrice(productEntity.getPrice());
            productEntity1.setIsActive(true);
            logger.info("Product updated: {}",productEntity1.toString());
            return toDTO(productRepository.save(productEntity1));
        }
        logger.info("Product saved{}",productEntity);
        return toDTO(productRepository.save(productEntity));
    }

    public List<ProductDTO> getAll() {
        logger.info("Get all products");
        return productRepository.findAllDTO();
    }

    public ProductDTO getById(Long id) {
        logger.info("Get product by id: {}", id);
        return productRepository.findByIdDTO(id);
    }

    public Boolean delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        if(productEntity != null) {
            logger.info("Delete product by id: {}", id);
            productRepository.delete(productEntity);
            return true;
        }
        return false;
    }

    public ProductDTO getByNameAndCategory(String name, String category) {
        logger.info("Get product by name: {} and category: {}", name, category);
        return productRepository.searchByNameAndCategoryDTO(name,category);
    }

    public List<ProductDTO> getByName(String name) {
        logger.info("Get product by name: {}", name);
        return productRepository.findByNameDTO(name);
    }
    public ProductDTO toDTO(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setName(productEntity.getName());
        productDTO.setCategory(productEntity.getCategory());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setStock(productEntity.getStock());
        productDTO.setIsActive(productEntity.getIsActive());
        productDTO.setCreatedAt(productEntity.getCreatedAt());
        return productDTO;
    }

    public ProductDTO updateById(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if(productEntity.isEmpty()) {
            logger.error("Product not found");
            throw new NotFoundException("Product not found");
        }
        productEntity.get().setIsActive(!productEntity.get().getIsActive());
       ;
        return  toDTO(productEntity.get());
    }
}
