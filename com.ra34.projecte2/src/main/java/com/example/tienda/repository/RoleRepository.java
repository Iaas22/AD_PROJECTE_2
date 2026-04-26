package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tienda.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
