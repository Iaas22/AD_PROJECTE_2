package com.example.tienda.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tienda.dto.AddressDTO;
import com.example.tienda.dto.CustomerDTO;
import com.example.tienda.dto.request.AddAddressesRequest;
import com.example.tienda.mapper.AddressMapper;
import com.example.tienda.mapper.CustomerMapper;
import com.example.tienda.model.Address;
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
        
    
    // Añade una lista de direcciones a un customer 
    @Transactional
    public CustomerDTO addAddressesToCustomer(Long customerId, AddAddressesRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer no trobat"));

        for (AddressDTO addressDTO : request.getAddresses()) {
            Address address = AddressMapper.toEntity(addressDTO);
            customer.addAddress(address);
        }

        Customer saved = customerRepository.save(customer);
        return CustomerMapper.toDTO(saved);
    }
    // Devuelve un customer por id con el email del usuario, nombre, telefono y direcciones
    @Transactional
    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer no trobat"));

        return CustomerMapper.toDTO(customer);
    }
}
