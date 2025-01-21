package com.dathihida.service.impl;

import com.dathihida.exception.ProductException;
import com.dathihida.model.Category;
import com.dathihida.model.Product;
import com.dathihida.model.Seller;
import com.dathihida.repository.CategoryRepository;
import com.dathihida.repository.ProductRepository;
import com.dathihida.request.CreateProductRequest;
import com.dathihida.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {
        Category category1 = getOrCreateCategory(request.getCategory(), 1, null);
        Category category2 = getOrCreateCategory(request.getCategory2(), 2, category1);
        Category category3 = getOrCreateCategory(request.getCategory3(), 3, category2);

        int discountPercentage = calculateDiscountPercentage(request.getMrpPrice(), request.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(request.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setSellingPrice(request.getSellingPrice());
        product.setImages(request.getImages());
        product.setMrpPrice(request.getMrpPrice());
        product.setSizes(request.getSizes());
        product.setDiscountPercent(discountPercentage);
        product.setQuantity(request.getQuantity());
        return productRepository.save(product);
    }

    private Category getOrCreateCategory(String categoryId, int level, Category parentCategory) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if (category == null) {
            category = new Category();
            category.setCategoryId(categoryId);
            category.setLevel(level);
            category.setParentCategory(parentCategory);
            category = categoryRepository.save(category);
        }
        return category;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0){
            throw new IllegalArgumentException("MrpPrice cannot be negative");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);

    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("product not found with id"+productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();

            if(category != null){
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }

            if(colors != null && !colors.isEmpty()){
                predicates.add(criteriaBuilder
                        .equal(root.get("color"), colors));
            }

            if(sizes != null && !sizes.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
            }

            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }

            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }

            if(minDiscount != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), minDiscount));
            }
            if(stock != null){
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable;
        if(sort!= null && !sort.isEmpty()){
            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        }else{
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
