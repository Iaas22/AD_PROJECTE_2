package com.example.tienda.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tienda.dto.CustomerDTO;
import com.example.tienda.mapper.CustomerMapper;
import com.example.tienda.model.Customer;
import com.example.tienda.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Borra todas las direcciones del customer pero mantiene su usuario.
    @Transactional
    public CustomerDTO deleteAllAddressesByCustomerId(Long customerId) {
        // Si no existe el customer, devuelve error
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer no trobat"));

        // Limpia la lista de direcciones y guarda el cambio
        customer.clearAddresses();
        Customer saved = customerRepository.save(customer);

        return CustomerMapper.toDTO(saved);
    }

    // Saca todos los customers y los convierte a DTO para devolverlos por API
    @Transactional
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toDTO).collect(Collectors.toList());
    }
}
