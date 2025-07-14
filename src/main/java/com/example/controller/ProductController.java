package com.example.controller;

import com.example.dto.OrderDTO;
import com.example.dto.ProductDTO;
import com.example.entity.ProductEntity;
import com.example.service.OrderService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;


    //Add
    @PostMapping
    public ResponseEntity<ProductEntity> add( @Valid @RequestBody ProductEntity productEntity) {

        System.out.println(productEntity.toString());
        return ResponseEntity.ok(productService.add(productEntity));
    }

    //List
    @GetMapping()
    public ResponseEntity<List<ProductDTO>>  getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    //ById
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Boolean status = productService.delete(id);
        if (status) {
            return ResponseEntity.ok("Deleted Product Successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //GetByNameAndCategory
    @GetMapping(value = "/customer/{name}&category={category}")
    public ResponseEntity<ProductDTO> getCustomerOrders(@PathVariable String name, @PathVariable String category) {
        return ResponseEntity.ok(productService.getByNameAndCategory(name,category));
    }
}
