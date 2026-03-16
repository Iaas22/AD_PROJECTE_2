package com.example.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.tienda.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);

    List<Product> findByPriceBetween(Double min, Double max);

    @Query("SELECT p FROM Product p WHERE p.stock > :minStock")
    List<Product> findProductsWithStockGreaterThan(@Param("minStock") int minStock);
}
