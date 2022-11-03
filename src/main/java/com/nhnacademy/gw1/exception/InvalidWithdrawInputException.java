package com.nhnacademy.gw1.exception;

public class InvalidWithdrawInputException extends RuntimeException {

  public InvalidWithdrawInputException(double inputMoney) {
    super(inputMoney + " is over your balance");
  }
}
