package com.example.tienda.dto.request;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderRequest {

    private Long customerId;
    private List<OrderProductRequest> products = new ArrayList<>();

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductRequest> products) {
        this.products = products;
    }
}
