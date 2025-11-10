package com.mycompany.ecommerce.backend.repository;
import com.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryIgnoreCase(String category, Pageable pageable);
    Page<Product> findByPriceBetween(Double min, Double max, Pageable pageable);
}
