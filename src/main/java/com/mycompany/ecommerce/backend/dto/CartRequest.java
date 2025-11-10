package com.ecommerce.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    private Long productId;
    private Integer quantity;
}
