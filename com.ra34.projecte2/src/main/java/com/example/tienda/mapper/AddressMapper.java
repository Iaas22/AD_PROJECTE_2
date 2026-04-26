package com.example.tienda.mapper;

import com.example.tienda.dto.AddressDTO;
import com.example.tienda.model.Address;

public class AddressMapper {

    private AddressMapper() {
    }

    public static AddressDTO toDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getAddressid());
        dto.setAddress(address.getAddress());
        dto.setCity(address.getCity());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setIsDefault(address.getIsDefault());
        return dto;
    }

    public static Address toEntity(AddressDTO dto) {
        Address address = new Address();
        address.setAddressid(dto.getId());
        address.setAddress(dto.getAddress());
        address.setCity(dto.getCity());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setIsDefault(dto.getIsDefault());
        return address;
    }
}
