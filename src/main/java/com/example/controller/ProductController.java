package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.dto.create.ProductCreateDTO;
import com.example.dto.update.ProductUpdateDTO;
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

    @PostMapping
    public ResponseEntity<ProductDTO> add( @Valid @RequestBody ProductCreateDTO productCreateDTO) {
        return ResponseEntity.ok(productService.add(productCreateDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,@Valid @RequestBody ProductUpdateDTO dto) {
        return ResponseEntity.ok(productService.updateById(id,dto));
    }
    //Product change
    @PutMapping("/change-status/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable Long id) {
        return ResponseEntity.ok(productService.changeStatusById(id));
    }
    @GetMapping()
    public ResponseEntity<List<ProductDTO>>  getAll() {
        return ResponseEntity.ok(productService.getAll());
    }
    //getById
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }
    @GetMapping("/search/{name}")
    public ResponseEntity< List<ProductDTO> > getByName(@PathVariable String name) {
        System.out.println(name);
        return ResponseEntity.ok(productService.getByName(name));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Boolean status = productService.delete(id);
        if (status) {
            return ResponseEntity.ok("Deleted Product Successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping(value = "/customer/{name}&category={category}")
    public ResponseEntity<ProductDTO> getCustomerOrders(@PathVariable String name, @PathVariable String category) {
        return ResponseEntity.ok(productService.getByNameAndCategory(name,category));
    }
}
