package com.example.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.tienda.dto.ProductDTO;
import com.example.tienda.model.Product;
import com.example.tienda.repository.ProductRepository;

public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findByStatusTrue().stream().map(this::toDTO).toList();
    }

    public ProductDTO getProductById(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producte no trobat"));
        return toDTO(p);
    }

    public ProductDTO addProduct(Product product) {
        product.setId(null);
        product.setStatus(true);
        return toDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(Long id, Product updated) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte no trobat"));

        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setStock(updated.getStock());
        p.setPrice(updated.getPrice());
        p.setRating(updated.getRating());
        p.setCondition(updated.getCondition());

        return toDTO(productRepository.save(p));
    }

    public ProductDTO updateStock(Long id, Integer stock) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte no trobat"));

        p.setStock(stock);
        return toDTO(productRepository.save(p));
    }

    // Se convierte producto a productDTO
    private ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setStock(p.getStock());
        dto.setPrice(p.getPrice());
        dto.setRating(p.getRating());
        dto.setCondition(p.getCondition());
        return dto;
    }
}

