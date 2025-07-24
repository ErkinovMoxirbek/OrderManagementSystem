package com.example.service;

import com.example.dto.ProductDTO;
import com.example.dto.create.ProductCreateDTO;
import com.example.dto.update.ProductUpdateDTO;
import com.example.entity.ProductEntity;
import com.example.exception.InsufficientStockException;
import com.example.exception.NotFoundException;
import com.example.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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
        //agar mahsulot bor bolsa qoshmay kopaytirib qoyadi
        if (productRepository.findByName(productCreateDTO.getName()) != null) {
            ProductEntity productEntity = productRepository.findByName(productCreateDTO.getName());
            productEntity.setStock(productEntity.getStock() + productCreateDTO.getStock());
            productEntity.setCategory(productEntity.getCategory() + productCreateDTO.getCategory());
            productEntity.setIsActive(true);
            productEntity.setVisible(true);
            productRepository.save(productEntity);
            logger.info("Product added successfully");
            return toDTO(productEntity);
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productCreateDTO.getName());
        productEntity.setPrice(productCreateDTO.getPrice());
        productEntity.setStock(productCreateDTO.getStock());
        productEntity.setCategory(productCreateDTO.getCategory());
        logger.info("Product saved{}",productEntity);
        return toDTO(productRepository.save(productEntity));
    }

    public PageImpl<ProductDTO> getAll(int page, int size) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ProductEntity> result = productRepository.findAllByVisibleTrue(pageable);

            List<ProductDTO> dtoList = new LinkedList<>();
            for (ProductEntity product: result.getContent()) {
                ProductDTO dto = new ProductDTO(product.getId(),product.getName(),product.getPrice(),product.getStock(),product.getCategory(),product.getIsActive());
                dtoList.add(dto);
            }
            logger.info("Get all products");
            return new PageImpl<>(dtoList, pageable, result.getTotalElements());
    }
    @Cacheable(value = "products", key = "#id")
    public ProductDTO getById(Long id) {
        logger.info("Get product by id: {}", id);
        return productRepository.findByIdDTO(id);
    }
    @CacheEvict(value = "products", key = "#id")
    public Boolean delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        if(productEntity != null) {
            logger.info("Delete product by id: {}", id);
            productEntity.setVisible(false);
            productEntity.setIsActive(false);
            productRepository.save(productEntity);
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
        return productDTO;
    }

    public ProductDTO updateById(Long id, ProductUpdateDTO productUpdateDTO) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (productEntityOptional.isEmpty()) {
            logger.error("Product not found");
            throw new NotFoundException("Product not found");
        }
        if (productUpdateDTO.getStock() == null || productUpdateDTO.getStock() <= 0) {
            logger.error("Stock should be greater than 0");
            throw new InsufficientStockException("Stock should be greater than 0");
        }
        ProductEntity productEntity = productEntityOptional.get();
        productEntity.setStock(productUpdateDTO.getStock());
        productEntity.setName(productUpdateDTO.getName());
        productEntity.setPrice(productUpdateDTO.getPrice());
        return toDTO(productRepository.save(productEntity));
    }
    public String changeStatusById(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if(productEntity.isEmpty()) {
            logger.error("Product not found");
            throw new NotFoundException("Product not found");
        }
        productEntity.get().setIsActive(!productEntity.get().getIsActive());
        productRepository.save(productEntity.get());
        return "Product status changed successfully";
    }
}
