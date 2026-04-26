package com.example.tienda.mapper;

import java.util.stream.Collectors;

import com.example.tienda.dto.UserDTO;
import com.example.tienda.dto.UserDetailsDTO;
import com.example.tienda.dto.UserRolesDTO;
import com.example.tienda.dto.request.CreateUserRequest;
import com.example.tienda.model.Customer;
import com.example.tienda.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setStatus(true);
        return user;
    }

    public static Customer toCustomerEntity(CreateUserRequest request, User user) {
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setStatus(true);
        return customer;
    }

    public static UserDetailsDTO toDetailsDTO(User user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        if (user.getCustomer() != null) {
            dto.setCustomer(CustomerMapper.toDTO(user.getCustomer()));
        }
        return dto;
    }

    public static UserRolesDTO toRolesDTO(User user) {
        UserRolesDTO dto = new UserRolesDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(RoleMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }
}
