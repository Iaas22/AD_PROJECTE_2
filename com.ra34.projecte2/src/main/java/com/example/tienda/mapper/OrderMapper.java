package com.example.tienda.mapper;

import java.util.stream.Collectors;

import com.example.tienda.dto.OrderDTO;
import com.example.tienda.dto.OrderItemDTO;
import com.example.tienda.model.Order;
import com.example.tienda.model.OrderItem;

public class OrderMapper {

    private OrderMapper() {}

    public static OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setItems(order.getItems().stream()
                .map(OrderMapper::toItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }
}