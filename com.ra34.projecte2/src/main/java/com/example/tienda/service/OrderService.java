package com.example.tienda.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tienda.dto.OrderDTO;
import com.example.tienda.dto.request.AddProductsToOrderRequest;
import com.example.tienda.dto.request.CreateOrderRequest;
import com.example.tienda.dto.request.OrderProductRequest;
import com.example.tienda.mapper.OrderMapper;
import com.example.tienda.model.Customer;
import com.example.tienda.model.Order;
import com.example.tienda.model.OrderItem;
import com.example.tienda.model.OrderStatus;
import com.example.tienda.model.Product;
import com.example.tienda.repository.CustomerRepository;
import com.example.tienda.repository.OrderRepository;
import com.example.tienda.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException("El customerId es obligatori");
        }
        validateProducts(request.getProducts());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer no trobat"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.PENDENT);
        order.setTotalAmount(BigDecimal.ZERO);

        addProducts(order, request.getProducts());

        Order saved = orderRepository.save(order);
        return OrderMapper.toDTO(saved);
    }

    @Transactional
    public OrderDTO addProductsToOrder(Long orderId, AddProductsToOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order no trobada"));

        validateProducts(request.getProducts());
        addProducts(order, request.getProducts());

        Order saved = orderRepository.save(order);
        return OrderMapper.toDTO(saved);
    }

    @Transactional
    public OrderDTO processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order no trobada"));

        if (order.getOrderStatus() != OrderStatus.PENDENT) {
            throw new IllegalArgumentException("Nomes es pot processar una ordre en estat PENDENT");
        }

        order.setOrderStatus(OrderStatus.PROCESSAT);
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

    private void validateProducts(List<OrderProductRequest> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("La comanda ha de contenir almenys un producte");
        }
    }

    private void addProducts(Order order, List<OrderProductRequest> products) {
        for (OrderProductRequest productRequest : products) {
            if (productRequest.getProductId() == null) {
                throw new IllegalArgumentException("Cada producte ha de tenir productId");
            }
            if (productRequest.getQuantity() == null || productRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException(
                        "La quantitat del producte " + productRequest.getProductId() + " ha de ser major que 0");
            }

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
    }
}
