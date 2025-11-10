package com.ecommerce.controller;
import com.ecommerce.dto.CartRequest;
import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping("/add/{userId}")
    public ResponseEntity<Cart> add(@PathVariable Long userId, @RequestBody CartRequest req) {
        return ResponseEntity.ok(cartService.addToCart(userId, req));
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<Cart> update(@PathVariable Long userId, @RequestBody CartRequest req) {
        return ResponseEntity.ok(cartService.updateQuantity(userId, req));
    }
    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<Cart> remove(@PathVariable Long userId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> get(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }
}
