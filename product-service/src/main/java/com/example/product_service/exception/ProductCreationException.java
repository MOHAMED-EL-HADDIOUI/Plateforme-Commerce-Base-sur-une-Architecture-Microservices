package com.example.product_service.exception;

public class ProductCreationException extends RuntimeException {

    public ProductCreationException(String message,Exception e) {
        super(message,e);
    }
}
