package com.example.controller;

import com.example.dto.OrderDTO;
import com.example.entity.OrderEntity;
import com.example.service.OrderService;
import com.example.status.OrderStatus;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //Add
    @PostMapping
    public ResponseEntity<OrderEntity> addOrder(@Valid @RequestBody OrderEntity orderEntity) {
        System.out.println(orderEntity.toString());
        return ResponseEntity.ok(orderService.addOrder(orderEntity));
    }

    //List
    @GetMapping()
    public ResponseEntity<List<OrderDTO>>  getAllOrders() {
            return ResponseEntity.ok(orderService.getAllOrders());
    }

    //ById
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderId(id));
    }

    //ById changeStatus
    @PutMapping(value = "/{id}/{status}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @PathVariable OrderStatus status) {
        OrderDTO student = orderService.updateService(id,status);
        return ResponseEntity.ok(student);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Boolean status = orderService.deleteOrder(id);
        if (status) {
            return ResponseEntity.ok("Deleted Order");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //getAllByEmail
    @GetMapping(value = "/customer/{email}")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }
}
