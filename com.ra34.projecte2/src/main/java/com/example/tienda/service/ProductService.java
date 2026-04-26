package com.example.tienda.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.dto.ProductDTO;
import com.example.tienda.model.Condition;
import com.example.tienda.model.Product;
import com.example.tienda.repository.ProductRepository;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;

@Service
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

            if (lineNumber == 1 && line.toLowerCase().contains("name")) {
                continue;
            }

            String[] fields = line.split(",");

            if (fields.length < 7) {
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
                p.setStatus(Boolean.parseBoolean(fields[6]));

                products.add(p);

            } catch (Exception e) {
                throw new Exception("Error en línea " + lineNumber + ": " + e.getMessage());
            }
        }
    }

    productRepository.saveAll(products);

    return products.size();
}


  public List<ProductDTO> searchByNamePrefix(String prefix) {
      return productRepository
            .findByNameStartingWithIgnoreCaseAndStatusTrue(prefix)
            .stream()
            .map(this::toDTO)
            .toList();
  }

  public List<ProductDTO> orderByPrice(String order) {

    List<Product> products;

    if (order.equalsIgnoreCase("asc")) {
        products = productRepository.findByStatusTrueOrderByPriceAsc();
    } else if (order.equalsIgnoreCase("desc")) {
        products = productRepository.findByStatusTrueOrderByPriceDesc();
    } else {
        throw new RuntimeException("El parámetre order ha de ser 'asc' o 'desc'");
    }

    return products.stream()
            .map(this::toDTO) 
            .toList();
    }

    public List<ProductDTO> filterProducts(BigDecimal min, BigDecimal max, String camp, String order, int limit) {

    List<Product> products = productRepository.filterByPriceRange(min, max);

    Comparator<Product> comparator;

    switch (camp.toLowerCase()) {
        case "price" -> comparator = Comparator.comparing(Product::getPrice);
        case "rating" -> comparator = Comparator.comparing(Product::getRating);
        case "name" -> comparator = Comparator.comparing(Product::getName);
        default -> throw new RuntimeException("Camp no vàlid: " + camp);
    }

    if (order.equalsIgnoreCase("desc")) {
        comparator = comparator.reversed();
    }

    return products.stream()
            .sorted(comparator)
            .limit(limit)
            .map(this::toDTO)
            .toList();
}

    public List<ProductDTO> top5QualityPrice() {

       List<Product> products = productRepository.findAllActive();

       Comparator<Product> comparator = Comparator.comparing(
            p -> Double.valueOf(
                    p.getRating().doubleValue() / p.getPrice().doubleValue()
            )
        );

        return products.stream()
            .sorted(comparator.reversed())
            .limit(5)
            .map(this::toDTO)
            .toList();
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

 
    public List<ProductDTO> searchByCondition(Condition condition) {
    return productRepository
            .findByConditionAndStatusTrue(condition)
            .stream()
            .map(this::toDTO)
            .toList();
}

    public List<ProductDTO> orderByRating(String order) {
    List<Product> products;

    if (order.equalsIgnoreCase("asc")) {
        products = productRepository.findByStatusTrueOrderByRatingAsc();
    } else if (order.equalsIgnoreCase("desc")) {
        products = productRepository.findByStatusTrueOrderByRatingDesc();
    } else {
        throw new RuntimeException("El paràmetre order ha de ser 'asc' o 'desc'");
    }

    return products.stream()
            .map(this::toDTO)
            .toList();
}

 
    public List<ProductDTO> filterByRating(BigDecimal min, BigDecimal max, String camp, String order, int limit) {

    List<Product> products = productRepository.filterByRatingRange(min, max);

    Comparator<Product> comparator;

    switch (camp.toLowerCase()) {
        case "price" -> comparator = Comparator.comparing(Product::getPrice);
        case "rating" -> comparator = Comparator.comparing(Product::getRating);
        case "name" -> comparator = Comparator.comparing(Product::getName);
        default -> throw new RuntimeException("Camp no vàlid: " + camp);
    }

    if (order.equalsIgnoreCase("desc")) {
        comparator = comparator.reversed();
    }

    return products.stream()
            .sorted(comparator)
            .limit(limit)
            .map(this::toDTO)
            .toList();
}


    public List<ProductDTO> top10NewByRating() {
    return productRepository.findTop10NewByRating()
            .stream()
            .map(this::toDTO)
            .toList();
}

   
    public List<ProductDTO> getProductsPaginated(int page) {
    Pageable pageable = PageRequest.of(page, 5);
    return productRepository.findActiveProducts(pageable)
            .stream()
            .map(this::toDTO)
            .toList();
}
}

