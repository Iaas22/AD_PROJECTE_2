package com.example.tienda.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tienda.dto.UserDTO;
import com.example.tienda.dto.UserDetailsDTO;
import com.example.tienda.dto.UserRolesDTO;
import com.example.tienda.dto.request.CreateUserRequest;
import com.example.tienda.mapper.UserMapper;
import com.example.tienda.model.Customer;
import com.example.tienda.model.Role;
import com.example.tienda.model.User;
import com.example.tienda.repository.CustomerRepository;
import com.example.tienda.repository.RoleRepository;
import com.example.tienda.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public UserDTO createUserWithCustomer(CreateUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email es obligatori");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ja existeix un usuari amb aquest email");
        }

        User user = UserMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        Customer customer = UserMapper.toCustomerEntity(request, savedUser);
        Customer savedCustomer = customerRepository.save(customer);

        savedUser.setCustomer(savedCustomer);

        return UserMapper.toDTO(savedUser);
    }

    @Transactional
    public UserDetailsDTO getUserWithCustomer(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        return UserMapper.toDetailsDTO(user);
    }

    @Transactional
    public UserRolesDTO removeRolesFromUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Cal informar la llista d'ids de rols");
        }

        Set<Role> rolesToRemove = new HashSet<>(roleRepository.findAllById(roleIds));
        user.getRoles().removeAll(rolesToRemove);

        User saved = userRepository.save(user);
        return UserMapper.toRolesDTO(saved);
    }
}
