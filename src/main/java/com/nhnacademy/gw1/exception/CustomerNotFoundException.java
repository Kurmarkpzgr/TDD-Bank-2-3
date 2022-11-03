package com.nhnacademy.gw1.exception;

public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException(String customerId) {
    super("Not found customer: " + customerId);
  }
}
