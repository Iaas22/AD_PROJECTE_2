package com.example.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tienda.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca un usuario por email.
    Optional<User> findByEmail(String email);

    // Comprueba si ya existe un email en la BD.
    boolean existsByEmail(String email);
}
