package com.nhnacademy.gw1.exception;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException(double inputMoney) {
    super("Invalid money input Exception " + inputMoney);
  }
}
