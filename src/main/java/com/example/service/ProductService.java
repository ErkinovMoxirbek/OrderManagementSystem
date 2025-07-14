package com.example.service;

import com.example.dto.OrderDTO;
import com.example.dto.ProductDTO;
import com.example.entity.OrderEntity;
import com.example.entity.ProductEntity;
import com.example.repository.ProductRepository;
import com.example.status.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductEntity add(ProductEntity productEntity) {
        ProductEntity productEntity1 = productRepository.findByName(productEntity.getName());
        if (productEntity1 != null) {
            System.out.println(productEntity1.toString());
            productEntity1.setStock(productEntity.getStock() + productEntity1.getStock());
            productEntity1.setPrice(productEntity.getPrice());
            return productRepository.save(productEntity1);
        }
        return productRepository.save(productEntity);
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAllDTO();
    }

    public ProductDTO getById(Long id) {
        return productRepository.findByIdDTO(id);
    }

    public Boolean delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        if(productEntity != null) {
            productRepository.delete(productEntity);
            return true;
        }
        return false;
    }

    public ProductDTO getByNameAndCategory(String name, String category) {
        return productRepository.searchByNameAndCategoryDTO(name,category);
    }
}
