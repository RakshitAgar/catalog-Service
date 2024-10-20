package com.example.catalog_service.Exceptions;

public class InvalidMenuItemCredentialsException extends RuntimeException {
  public InvalidMenuItemCredentialsException(String message) {
    super(message);
  }
}
