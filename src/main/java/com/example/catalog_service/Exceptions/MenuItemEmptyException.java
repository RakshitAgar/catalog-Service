package com.example.catalog_service.Exceptions;

public class MenuItemEmptyException extends RuntimeException {
    public MenuItemEmptyException(String message) {
        super(message);
    }
}
