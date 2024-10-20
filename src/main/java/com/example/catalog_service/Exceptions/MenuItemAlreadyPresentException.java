package com.example.catalog_service.Exceptions;

public class MenuItemAlreadyPresentException extends RuntimeException {
  public MenuItemAlreadyPresentException(String message) {
    super(message);
  }
}
