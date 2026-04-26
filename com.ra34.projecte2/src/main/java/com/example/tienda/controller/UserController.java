package com.example.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.dto.request.CreateUserRequest;
import com.example.tienda.dto.request.RemoveUserRolesRequest;
import com.example.tienda.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Crea un usuario nuevo (y su customer) con los datos que llegan en el body.
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUserWithCustomer(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Devuelve un usuario por id junto con los datos del customer relacionado.
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserWithCustomer(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Quita roles al usuario, pero no borra los roles de la tabla Role.
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<?> removeRoles(@PathVariable Long id, @RequestBody RemoveUserRolesRequest request) {
        try {
            return ResponseEntity.ok(userService.removeRolesFromUser(id, request.getRoleIds()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
