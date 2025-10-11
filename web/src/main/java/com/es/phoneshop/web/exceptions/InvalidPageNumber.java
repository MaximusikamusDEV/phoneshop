package com.es.phoneshop.web.exceptions;

public class InvalidPageNumber extends RuntimeException {
  public InvalidPageNumber(String message) {
    super(message);
  }
}
