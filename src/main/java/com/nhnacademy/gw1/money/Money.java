package com.nhnacademy.gw1.money;

import java.util.Objects;

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

  @Override
  public String toString() {
    if (this.currency == Currency.WON) {
      return (long)this.amount + "원";
    } else if (this.currency == Currency.EURO) {
      return this.amount + "€";
    }
    return this.amount + "$";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Money money = (Money) o;
    return Double.compare(money.amount, amount) == 0 && currency == money.currency;
  }

  @Override
  public int hashCode() {
    return Objects.hash(currency, amount);
  }
}
