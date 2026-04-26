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

    @Transactional
    public CustomerDTO deleteAllAddressesByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer no trobat"));

        customer.clearAddresses();
        Customer saved = customerRepository.save(customer);

        return CustomerMapper.toDTO(saved);
    }

    @Transactional
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toDTO).collect(Collectors.toList());
    }
}
