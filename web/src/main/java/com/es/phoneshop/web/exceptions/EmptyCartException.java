package com.es.phoneshop.web.exceptions;

public class EmptyCartException extends RuntimeException {
  public EmptyCartException(String message) {
    super(message);
  }
}
