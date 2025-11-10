package com.mycompany.ecommerce.backend.service;
import com.ecommerce.dto.CartRequest;
import com.ecommerce.entity.*;
import com.ecommerce.exception.ApiException;
import com.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    public Cart getCartByUserId(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found"));
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart c = Cart.builder().user(user).totalPrice(0.0).items(new ArrayList<>()).build();
            return cartRepository.save(c);
        });
    }
    public Cart addToCart(Long userId, CartRequest req) {
        var cart = getCartByUserId(userId);
        var product = productRepository.findById(req.getProductId()).orElseThrow(() -> new ApiException("Product not found"));
        Optional<CartItem> existing = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(product.getId())).findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + req.getQuantity());
        } else {
            CartItem item = CartItem.builder().product(product).quantity(req.getQuantity()).build();
            cart.getItems().add(item);
        }
        recalc(cart);
        return cartRepository.save(cart);
    }
    public Cart updateQuantity(Long userId, CartRequest req) {
        var cart = getCartByUserId(userId);
        cart.getItems().stream().filter(i -> i.getProduct().getId().equals(req.getProductId())).findFirst().ifPresentOrElse(i -> i.setQuantity(req.getQuantity()), () -> {throw new ApiException("Cart item not found");});
        recalc(cart);
        return cartRepository.save(cart);
    }
    public Cart removeFromCart(Long userId, Long productId) {
        var cart = getCartByUserId(userId);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        recalc(cart);
        return cartRepository.save(cart);
    }
    private void recalc(Cart cart) {
        double total = cart.getItems().stream().mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity()).sum();
        cart.setTotalPrice(total);
    }
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }
}
