package com.mycompany.ecommerce.backend.controller;

import com.mycompany.ecommerce.backend.dto.OrderRequest;
import com.mycompany.ecommerce.backend.entity.OrderEntity;
import com.mycompany.ecommerce.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrderEntity> checkout(@PathVariable Long userId, @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.checkout(userId, request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderEntity>> history(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderEntity> updateStatus(@PathVariable Long id, @RequestParam OrderEntity.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
