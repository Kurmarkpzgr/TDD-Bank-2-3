package com.nhnacademy.gw1.customer;

import com.nhnacademy.gw1.money.Money;
import java.util.List;

public class Customer {
  private final String customerId;
  private String password;

  private List<Money> balance;
  private final int WON =0;
  private final int DOLLAR =1;

  public Customer(String customerId, String password) {
    this.customerId = customerId;
    this.password = password;
  }

  public String getCustomerId() {
    return this.customerId;
  }

  public Money getWonBalance() {
    return balance.get(WON);
  }
}
