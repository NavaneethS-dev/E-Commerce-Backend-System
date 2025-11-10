package com.mycompany.ecommerce.backend.service;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ApiException;
import com.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public ProductDto create(ProductDto dto) {
        Product p = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .rating(dto.getRating())
                .build();
        productRepository.save(p);
        return toDto(p);
    }
    public Page<ProductDto> list(int page, int size, String category, Double minPrice, Double maxPrice) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> pageRes;
        if (category != null) pageRes = productRepository.findByCategoryIgnoreCase(category, pageable);
        else if (minPrice != null && maxPrice != null) pageRes = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        else pageRes = productRepository.findAll(pageable);
        return pageRes.map(this::toDto);
    }
    public ProductDto get(Long id) {
        var p = productRepository.findById(id).orElseThrow(() -> new ApiException("Product not found"));
        return toDto(p);
    }
    public ProductDto update(Long id, ProductDto dto) {
        var p = productRepository.findById(id).orElseThrow(() -> new ApiException("Product not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setCategory(dto.getCategory());
        p.setImageUrl(dto.getImageUrl());
        p.setRating(dto.getRating());
        productRepository.save(p);
        return toDto(p);
    }
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
    public void reduceStock(Long productId, int qty) {
        var p = productRepository.findById(productId).orElseThrow(() -> new ApiException("Product not found"));
        if (p.getStock() < qty) throw new ApiException("Out of stock");
        p.setStock(p.getStock()-qty);
        productRepository.save(p);
    }
    private ProductDto toDto(Product p) {
        return ProductDto.builder().id(p.getId()).name(p.getName()).description(p.getDescription()).price(p.getPrice()).stock(p.getStock()).category(p.getCategory()).imageUrl(p.getImageUrl()).rating(p.getRating()).build();
    }
}
