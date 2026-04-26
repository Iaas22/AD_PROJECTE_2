package com.example.tienda.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tienda.dto.OrderDTO;
import com.example.tienda.dto.request.AddProductsToOrderRequest;
import com.example.tienda.dto.request.OrderProductRequest;
import com.example.tienda.mapper.OrderMapper;
import com.example.tienda.model.Order;
import com.example.tienda.model.OrderItem;
import com.example.tienda.model.OrderStatus;
import com.example.tienda.model.Product;
import com.example.tienda.repository.OrderRepository;
import com.example.tienda.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderDTO addProductsToOrder(Long orderId, AddProductsToOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order no trobada"));

        for (OrderProductRequest productRequest : request.getProducts()) {
            Product product = productRepository.findById(productRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producte no trobat amb id: " + productRequest.getProductId()));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(productRequest.getQuantity());
            item.setUnitPrice(product.getPrice());

            order.addItem(item);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(productRequest.getQuantity()));
            order.setTotalAmount(order.getTotalAmount().add(itemTotal));
        }

        Order saved = orderRepository.save(order);
        return OrderMapper.toDTO(saved);
    }

    @Transactional
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order no trobada"));

        if (order.getOrderStatus() != OrderStatus.PENDENT) {
            throw new IllegalArgumentException("Només es pot cancelar una ordre en estat PENDENT");
        }

        order.setOrderStatus(OrderStatus.CANCELAT);
        Order saved = orderRepository.save(order);
        return OrderMapper.toDTO(saved);
    }
}