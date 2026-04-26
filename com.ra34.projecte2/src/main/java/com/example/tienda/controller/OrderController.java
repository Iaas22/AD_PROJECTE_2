package com.example.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.dto.request.AddProductsToOrderRequest;
import com.example.tienda.dto.request.CreateOrderRequest;
import com.example.tienda.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(orderService.createOrder(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/products")
    public ResponseEntity<?> addProducts(@PathVariable Long id,
            @RequestBody AddProductsToOrderRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(orderService.addProductsToOrder(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<?> processOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.processOrder(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.cancelOrder(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
