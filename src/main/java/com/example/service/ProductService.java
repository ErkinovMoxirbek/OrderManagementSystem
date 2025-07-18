package com.example.service;

import com.example.dto.ProductDTO;
import com.example.entity.ProductEntity;
import com.example.exception.BadRequestException;
import com.example.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    public ProductEntity add(ProductEntity productEntity) {
        ProductEntity productEntity1 = productRepository.findByName(productEntity.getName());
        //agar mahsulot bor bolsa qoshmay kopaytirib qoyadi
        if (productEntity.getStock() <= 0){
            logger.warn("Product stock is zero.");
            throw new BadRequestException("Product stock is zero.");
        }else if (productEntity1 != null) {
            productEntity1.setStock(productEntity.getStock() + productEntity1.getStock());
            productEntity1.setPrice(productEntity.getPrice());
            productEntity1.setIsActive(true);
            logger.info("Product updated: {}",productEntity1.toString());
            return productRepository.save(productEntity1);
        }
        logger.info("Product saved{}",productEntity);
        return productRepository.save(productEntity);
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
}
