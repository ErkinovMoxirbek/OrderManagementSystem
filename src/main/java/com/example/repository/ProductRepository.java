package com.example.repository;

import com.example.dto.OrderDTO;
import com.example.dto.ProductDTO;
import com.example.entity.OrderEntity;
import com.example.entity.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    ProductEntity findByName(String name);
    @Query("""
        SELECT new com.example.dto.ProductDTO(
            p.id,
            p.name,
            p.price,
            p.stock,
            p.category,
            p.isActive,
            p.createdAt
        )
        FROM ProductEntity p
    """)
    List<ProductDTO> findAllDTO();

    @Query("""
        SELECT new com.example.dto.ProductDTO(
            p.id,
            p.name,
            p.price,
            p.stock,
            p.category,
            p.isActive,
            p.createdAt
        )
        FROM ProductEntity p
        where p.id = :id
    """)
    ProductDTO findByIdDTO(@Param("id") Long id);

@Query("""
        SELECT new com.example.dto.ProductDTO(
            p.id,
            p.name,
            p.price,
            p.stock,
            p.category,
            p.isActive,
            p.createdAt
        )
        FROM ProductEntity p
        where p.name = :name and p.category = :category and p.isActive = :isActive
    """)
    ProductDTO searchByNameAndCategoryDTO(@Param("name") String name, @Param("category") String category);


    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.name = :#{#entity.name}, p.price = :#{#entity.price}, p.stock = :#{#entity.stock}, p.category = :#{#entity.category}, p.isActive = :#{#entity.isActive} WHERE p.name = :name and p.category = :category")
    ProductEntity updateById(@Param("name") String name, @Param("category") String category, @Param("entity") ProductEntity entity);


}
