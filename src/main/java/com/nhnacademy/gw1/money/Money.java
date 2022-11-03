package com.nhnacademy.gw1.money;

public class Money {

  private final Currency currency;
  private final double amount;

  public Money(Currency currency, double amount) {
    this.currency = currency;
    this.amount = amount;
  }

  public double getAmount() {
    return this.amount;
  }

  public Currency getCurrency() {
    return this.currency;
  }

  public String toString() {
    if (this.currency == Currency.WON) {
      return this.amount + "원";
    }
    return this.amount + "$";
  }
}
