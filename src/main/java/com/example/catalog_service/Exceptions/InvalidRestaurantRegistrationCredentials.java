package com.example.catalog_service.Exceptions;

public class InvalidRestaurantRegistrationCredentials extends RuntimeException {
    public InvalidRestaurantRegistrationCredentials(String message) {
        super(message);
    }
}
