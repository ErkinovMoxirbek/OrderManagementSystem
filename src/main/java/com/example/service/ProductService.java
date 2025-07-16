package com.example.service;

import com.example.dto.ProductDTO;
import com.example.entity.ProductEntity;
import com.example.exception.BadRequestException;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductEntity add(ProductEntity productEntity) {
        ProductEntity productEntity1 = productRepository.findByName(productEntity.getName());
        //agar mahsulot bor bolsa qoshmay kopaytirib qoyadi
        if (productEntity.getStock() <= 0){
            throw new BadRequestException("Product stock is zero.");
        }else if (productEntity1 != null) {
            System.out.println(productEntity1);
            productEntity1.setStock(productEntity.getStock() + productEntity1.getStock());
            productEntity1.setPrice(productEntity.getPrice());
            productEntity1.setIsActive(true);
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

    public List<ProductDTO> getByName(String name) {
        return productRepository.findByNameDTO(name);
    }
}
