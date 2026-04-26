package com.example.tienda.mapper;

import java.util.stream.Collectors;

import com.example.tienda.dto.CustomerDTO;
import com.example.tienda.model.Address;
import com.example.tienda.model.Customer;
import com.example.tienda.model.User;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getUser().getEmail());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhone(customer.getPhone());
        dto.setAddresses(customer.getAddresses().stream().map(AddressMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public static Customer toEntity(CustomerDTO dto, User user) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setUser(user);
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        if (dto.getAddresses() != null) {
            for (com.example.tienda.dto.AddressDTO addressDTO : dto.getAddresses()) {
                Address address = AddressMapper.toEntity(addressDTO);
                customer.addAddress(address);
            }
        }
        return customer;
    }
}
