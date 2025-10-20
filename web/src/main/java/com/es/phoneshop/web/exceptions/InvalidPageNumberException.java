package com.es.phoneshop.web.exceptions;

public class InvalidPageNumberException extends RuntimeException {
  public InvalidPageNumberException(String message) {
    super(message);
  }
}
