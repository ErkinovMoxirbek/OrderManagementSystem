package com.example.controller;

import com.example.dto.OrderItemDTO;
import com.example.dto.create.OrderItemCreateDTO;
import com.example.entity.OrderEntity;
import com.example.entity.OrderItemEntity;
import com.example.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;
    @PostMapping
    public ResponseEntity<OrderItemDTO> addOrder(@Valid @RequestBody OrderItemCreateDTO orderItemCreateDTO) {
        return ResponseEntity.ok(orderItemService.add(orderItemCreateDTO));
    }
    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrders() {
        return ResponseEntity.ok(orderItemService.getAll());
    }

    //Update kerakmikan deb oylanib turibman?

}
