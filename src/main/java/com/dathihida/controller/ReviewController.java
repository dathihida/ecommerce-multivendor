package com.dathihida.controller;

import com.dathihida.model.Product;
import com.dathihida.model.Review;
import com.dathihida.model.User;
import com.dathihida.request.CreateReviewRequest;
import com.dathihida.response.ApiResponse;
import com.dathihida.service.ProductService;
import com.dathihida.service.ReviewService;
import com.dathihida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> createReview(@PathVariable Long productId,
                                               @RequestBody CreateReviewRequest request,
                                               @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);

        Review review = reviewService.createReview(request, user, product);

        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public  ResponseEntity<Review> updateReview(@RequestBody CreateReviewRequest request,
                                                @PathVariable Long reviewId,
                                                @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Review review = reviewService.updateReview(
                                    reviewId,
                                    request.getReviewerText(),
                                    request.getReviewRating(),
                                    user.getId());
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,
                                                    @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResponse response = new ApiResponse();
        response.setMessage("Review deleted");
        return ResponseEntity.ok(response);
    }
}
