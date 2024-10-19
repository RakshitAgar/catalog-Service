package com.example.catalog_service.Exceptions;

public class RestaurantAlreadyExistException extends RuntimeException {
    public RestaurantAlreadyExistException(String message) {
        super(message);
    }
}
