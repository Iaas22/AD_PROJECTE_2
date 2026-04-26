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
import com.example.tienda.dto.request.UpdateUserRequest;
import com.example.tienda.mapper.UserMapper;
import com.example.tienda.model.Customer;
import com.example.tienda.model.Role;
import com.example.tienda.model.User;
import com.example.tienda.repository.CustomerRepository;
import com.example.tienda.repository.RoleRepository;
import com.example.tienda.repository.UserRepository;
import com.example.tienda.dto.request.UpdateUserRequest;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Crea user y customer en la misma transaccion para que se guarden juntos
    @Transactional
    public UserDTO createUserWithCustomer(CreateUserRequest request) {
        // Si no hay un email no se puede crear el usuario.
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email es obligatori");
        }
        // Evita usuarios repetidos con el mismo email.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ja existeix un usuari amb aquest email");
        }

        // Pasa los datos del request a User.
        User user = UserMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        // Crea el customer enlazado al user
        Customer customer = UserMapper.toCustomerEntity(request, savedUser);
        Customer savedCustomer = customerRepository.save(customer);

        savedUser.setCustomer(savedCustomer);

        // Solo devolvemos el DTO de usuario sin password
        return UserMapper.toDTO(savedUser);
    }

    // Busca un usuario por id y devuelve tambien sus datos de customer.
    @Transactional
    public UserDetailsDTO getUserWithCustomer(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        return UserMapper.toDetailsDTO(user);
    }

    // Quita roles al usuario sin borrar los roles de la base de datos.
    @Transactional
    public UserRolesDTO removeRolesFromUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        // Se necesita al menos un id de rol para quitar.
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Cal informar la llista d'ids de rols");
        }

        // Cargamos los roles y los quitamos de la lista del usuario.
        Set<Role> rolesToRemove = new HashSet<>(roleRepository.findAllById(roleIds));
        user.getRoles().removeAll(rolesToRemove);

        User saved = userRepository.save(user);
        return UserMapper.toRolesDTO(saved);
    }

    // Modifica los datos del usuario o su customer en una misma transaccion.
    @Transactional
    public UserDetailsDTO updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            boolean emailUsedByOther = userRepository.findByEmail(request.getEmail())
                    .filter(u -> !u.getId().equals(userId))
                    .isPresent();
            if (emailUsedByOther) {
                throw new IllegalArgumentException("Ja existeix un usuari amb aquest email");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        userRepository.save(user);

        Customer customer = user.getCustomer();
        if (customer != null) {
            if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
                customer.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null && !request.getLastName().isBlank()) {
                customer.setLastName(request.getLastName());
            }
            if (request.getPhone() != null && !request.getPhone().isBlank()) {
                customer.setPhone(request.getPhone());
            }
            customerRepository.save(customer);
        }

        return UserMapper.toDetailsDTO(user);
    }
}
