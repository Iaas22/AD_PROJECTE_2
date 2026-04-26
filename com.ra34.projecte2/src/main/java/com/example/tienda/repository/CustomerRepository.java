package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tienda.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
