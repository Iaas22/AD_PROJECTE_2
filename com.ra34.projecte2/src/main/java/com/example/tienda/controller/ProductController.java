package com.example.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.model.Product;
import com.example.tienda.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Consultar todos los productos
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Consultar producto por Id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Añadir producto
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Product product) {
        return ResponseEntity.status(201).body(productService.addProduct(product));
    }

    // Modificar todos los campos, se encuentra por Id
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    // Modificar solo el stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        return ResponseEntity.ok(productService.updateStock(id, stock));
    }
}
