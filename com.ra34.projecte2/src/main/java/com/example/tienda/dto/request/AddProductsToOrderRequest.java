package com.example.tienda.dto.request;

import java.util.ArrayList;
import java.util.List;

public class AddProductsToOrderRequest {

    private List<OrderProductRequest> products = new ArrayList<>();

    public List<OrderProductRequest> getProducts() { return products; }
    public void setProducts(List<OrderProductRequest> products) { this.products = products; }
}