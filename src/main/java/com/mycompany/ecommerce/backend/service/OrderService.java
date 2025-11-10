package com.mycompany.ecommerce.backend.service;

import com.mycompany.ecommerce.backend.dto.OrderRequest;
import com.mycompany.ecommerce.backend.entity.*;
import com.mycompany.ecommerce.backend.exception.ApiException;
import com.mycompany.ecommerce.backend.repository.OrderRepository;
import com.mycompany.ecommerce.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService,
                        ProductService productService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    public OrderEntity checkout(Long userId, OrderRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        var cart = cartService.getCartByUserId(userId);
        if (cart.getItems().isEmpty()) throw new ApiException("Cart is empty");

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setPaymentStatus(OrderEntity.PaymentStatus.PENDING);
        order.setOrderStatus(OrderEntity.OrderStatus.PLACED);

        for (CartItem ci : cart.getItems()) {
            if (ci.getProduct().getStock() < ci.getQuantity()) {
                throw new ApiException("Out of stock: " + ci.getProduct().getName());
            }
            OrderItem item = new OrderItem();
            item.setProduct(ci.getProduct());
            item.setQuantity(ci.getQuantity());
            item.setPrice(ci.getProduct().getPrice());
            order.getItems().add(item);
        }

        boolean success = simulatePayment(request.getPaymentMode());
        order.setPaymentStatus(success ? OrderEntity.PaymentStatus.SUCCESS : OrderEntity.PaymentStatus.FAILED);
        order.setOrderStatus(success ? OrderEntity.OrderStatus.PLACED : OrderEntity.OrderStatus.CANCELLED);

        OrderEntity savedOrder = orderRepository.save(order);

        if (success) {
            for (OrderItem item : savedOrder.getItems()) {
                productService.reduceStock(item.getProduct().getId(), item.getQuantity());
            }
        }

        cartService.clearCart(cart);
        return savedOrder;
    }

    private boolean simulatePayment(String mode) {
        return Math.random() > 0.1; // 90% success chance
    }

    public List<OrderEntity> getUserOrders(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));
        return orderRepository.findByUser(user);
    }

    public OrderEntity getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApiException("Order not found"));
    }

    public OrderEntity updateOrderStatus(Long id, OrderEntity.OrderStatus status) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ApiException("Order not found"));
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
}
