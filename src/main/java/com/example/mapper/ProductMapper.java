package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.dto.create.ProductCreateDTO;
import com.example.entity.ProductEntity;

public interface ProductMapper {
    String name();



    static ProductDTO toDTO(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCategory(entity.getCategory());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
    static ProductEntity toEntity(ProductCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(dto.getName());
        productEntity.setPrice(dto.getPrice());
        productEntity.setStock(dto.getStock());
        productEntity.setCategory(dto.getCategory());
        return productEntity;
    }
}
