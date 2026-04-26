package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tienda.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}