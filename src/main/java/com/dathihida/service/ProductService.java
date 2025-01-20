package com.dathihida.service;

import com.dathihida.model.Product;
import com.dathihida.model.Seller;
import com.dathihida.request.CreateProductRequest;

public interface ProductService {
    public Product createProduct(CreateProductRequest request, Seller seller);
}
