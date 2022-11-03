package com.nhnacademy.gw1.exception;

import com.nhnacademy.gw1.money.Currency;

public class EqualCurrencyException extends RuntimeException {

  public EqualCurrencyException(Currency currency) {
    super("You are trying to exchange same " + currency);
  }
}
