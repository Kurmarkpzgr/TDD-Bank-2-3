package com.nhnacademy.gw1.exception;

import com.nhnacademy.gw1.money.Money;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException(double inputMoney) {
    super("Invalid money input Exception " + inputMoney);
  }
}
