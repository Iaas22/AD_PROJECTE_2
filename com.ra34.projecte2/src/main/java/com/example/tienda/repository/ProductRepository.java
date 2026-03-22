package com.example.tienda.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.tienda.model.Product;
import com.example.tienda.model.Condition;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);

    List<Product> findByStatusTrue();

    List<Product> findByPriceBetween(Double min, Double max);

    List<Product> findByNameStartingWithIgnoreCaseAndStatusTrue(String prefix);

    List<Product> findByStatusTrueOrderByPriceAsc();
    List<Product> findByStatusTrueOrderByPriceDesc();


    @Query("SELECT p FROM Product p WHERE p.stock > :minStock")
    List<Product> findProductsWithStockGreaterThan(@Param("minStock") int minStock);


    @Query("SELECT p FROM Product p WHERE p.status = true AND p.price BETWEEN :min AND :max")
    List<Product> filterByPriceRange(
        @Param("min") BigDecimal min,
        @Param("max") BigDecimal max
    );


    @Query("SELECT p FROM Product p WHERE p.status = true")
    List<Product> findAllActive();

    // Query Method: filtra per condition i status=true
    List<Product> findByConditionAndStatusTrue(Condition condition);

// Query Method: ordena per rating ASC o DESC (dos mètodes)
    List<Product> findByStatusTrueOrderByRatingAsc();
    List<Product> findByStatusTrueOrderByRatingDesc();  


}
