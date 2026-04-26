package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tienda.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
