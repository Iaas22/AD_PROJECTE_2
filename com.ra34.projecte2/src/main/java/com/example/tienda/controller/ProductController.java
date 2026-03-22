package com.example.tienda.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.model.Product;
import com.example.tienda.service.ProductService;
import com.example.tienda.model.Condition;

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

    // Carga de CSV
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file) {
       try {
          int inserted = productService.loadCSV(file);
          return ResponseEntity.ok("Registres inserits: " + inserted);
        } catch (Exception e) {
          return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}

    @GetMapping("/search/name")
    public ResponseEntity<?> searchByName(@RequestParam String prefix) {
       return ResponseEntity.ok(productService.searchByNamePrefix(prefix));
    }

    @GetMapping("/search/order")
    public ResponseEntity<?> orderByPrice(@RequestParam String order) {
       return ResponseEntity.ok(productService.orderByPrice(order));
    }


    @GetMapping("/search/filter")
    public ResponseEntity<?> filterProducts(
        @RequestParam BigDecimal min,
        @RequestParam BigDecimal max,
        @RequestParam String camp,
        @RequestParam String order,
        @RequestParam int limit) {

       return ResponseEntity.ok(
            productService.filterProducts(min, max, camp, order, limit)
        );
    }


    @GetMapping("/search/top5")
      public ResponseEntity<?> top5QualityPrice() {
        return ResponseEntity.ok(productService.top5QualityPrice());
    }


    @PatchMapping("/{id}/price")
    public ResponseEntity<?> updatePrice(@PathVariable Long id, @RequestParam Double price) {
    return ResponseEntity.ok(productService.updatePrice(id, price));
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.ok("Producte eliminat");
}

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> deleteLogic(@PathVariable Long id) {
    return ResponseEntity.ok(productService.deleteLogic(id));
}

    
    @GetMapping("/search/condition")
    public ResponseEntity<?> searchByCondition(@RequestParam Condition condition) {
    return ResponseEntity.ok(productService.searchByCondition(condition));
}

    @GetMapping("/search/rating-order")
    public ResponseEntity<?> orderByRating(@RequestParam String order) {
    return ResponseEntity.ok(productService.orderByRating(order));
}


    @GetMapping("/search/rating-filter")
    public ResponseEntity<?> filterByRating(
        @RequestParam BigDecimal ratingMin,
        @RequestParam BigDecimal ratingMax,
        @RequestParam String camp,
        @RequestParam String order,
        @RequestParam int limit) {

    return ResponseEntity.ok(
            productService.filterByRating(ratingMin, ratingMax, camp, order, limit)
    );
}

    @GetMapping("/search/top10new")
    public ResponseEntity<?> top10NewByRating() {
    return ResponseEntity.ok(productService.top10NewByRating());
}
}
