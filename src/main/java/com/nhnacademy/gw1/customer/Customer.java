package com.nhnacademy.gw1.customer;

import com.nhnacademy.gw1.money.Currency;
import com.nhnacademy.gw1.money.Money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Customer {
  private final String customerId;
  private String password;

  private HashMap<Integer, Money> balance;


  public Customer(String customerId, String password) {
    this.customerId = customerId;
    this.password = password;

    balance = new HashMap<>();
    balance.put(Currency.WON.ordinal(), new Money(Currency.WON, 0));
    balance.put(Currency.DOLLAR.ordinal(), new Money(Currency.DOLLAR, 0));

  }

  public String getCustomerId() {
    return this.customerId;
  }

  public Money getBalance(Currency balanceCurrency) {
    return balance.get(balanceCurrency.ordinal());
  }
}
