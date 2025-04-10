package com.dathihida.service;

import com.dathihida.exception.ProductException;
import com.dathihida.model.Product;
import com.dathihida.model.Seller;
import com.dathihida.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest request, Seller seller);
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProducts(String query);
    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer discountPercent,
            String sort,
            String stock,
            Integer pageNumber
    );
    List<Product> getProductBySellerId(Long sellerId);
}
