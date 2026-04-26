package com.example.tienda.dto.request;

import java.util.ArrayList;
import java.util.List;
import com.example.tienda.dto.AddressDTO;

public class AddAddressesRequest {

    private List<AddressDTO> addresses = new ArrayList<>();

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }
}