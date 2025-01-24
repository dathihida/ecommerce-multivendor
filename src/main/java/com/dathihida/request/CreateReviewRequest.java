package com.dathihida.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateReviewRequest {
    private String reviewerText;
    private double reviewRating;
    private List<String> productImages;
}
