package com.ecommerce.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 2000)
    private String description;
    private Double price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private Double rating;
}
