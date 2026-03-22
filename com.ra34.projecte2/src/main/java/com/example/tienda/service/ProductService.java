package com.example.tienda.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.dto.ProductDTO;
import com.example.tienda.model.Condition;
import com.example.tienda.model.Product;
import com.example.tienda.repository.ProductRepository;

import jakarta.transaction.Transactional;

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

    @Transactional
public int loadCSV(MultipartFile file) throws Exception {

    if (file.isEmpty()) {
        throw new Exception("L'Arxiu esta buit");
    }

    List<Product> products = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

        String line;
        int lineNumber = 0;

        while ((line = br.readLine()) != null) {
            lineNumber++;

            // Saltar cabecera si existe
            if (lineNumber == 1 && line.toLowerCase().contains("name")) {
                continue;
            }

            String[] fields = line.split(",");

            if (fields.length < 6) {
                throw new Exception("Error en línea " + lineNumber + ": format incorrecte");
            }

            try {
                Product p = new Product();
                p.setName(fields[0]);
                p.setDescription(fields[1]);
                p.setStock(Integer.parseInt(fields[2]));
                p.setPrice(new BigDecimal(fields[3]));
                p.setRating(new BigDecimal(fields[4]));
                p.setCondition(Condition.valueOf(fields[5].toUpperCase()));
                p.setStatus(true);

                products.add(p);

            } catch (Exception e) {
                throw new Exception("Error en línea " + lineNumber + ": " + e.getMessage());
            }
        }
    }

    productRepository.saveAll(products);

    return products.size();
}

    public ProductDTO updatePrice(Long id, Double price) {
    Product p = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producte no trobat"));

    p.setPrice(BigDecimal.valueOf(price));
    return toDTO(productRepository.save(p));
}
    public void deleteProduct(Long id) {
    productRepository.deleteById(id);
}

    public ProductDTO deleteLogic(Long id) {
    Product p = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producte no trobat"));

    p.setStatus(false);
    return toDTO(productRepository.save(p));
}
}

