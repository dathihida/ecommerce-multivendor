package com.dathihida.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobeException {
    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException sellerException,
                                                               WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(sellerException.getMessage());
        errorDetails.setDetails(webRequest.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productExceptionHandler(SellerException sellerException,
                                                               WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(sellerException.getMessage());
        errorDetails.setDetails(webRequest.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
