package com.nhnacademy.gw1.customer;

import com.nhnacademy.gw1.money.Currency;
import com.nhnacademy.gw1.money.Money;
import java.util.HashMap;

public class Customer {

  private final String customerId;
  private String password;

  private final HashMap<Integer, Money> balance = new HashMap<>() {{
      put(Currency.WON.ordinal(), new Money(Currency.WON, 0));
      put(Currency.DOLLAR.ordinal(), new Money(Currency.DOLLAR, 0));
      put(Currency.EURO.ordinal(), new Money(Currency.EURO, 0));
    }};


  public Customer(String customerId, String password) {
    this.customerId = customerId;
    this.password = password;


  }

  public String getCustomerId() {
    return this.customerId;
  }

  public Money getBalance(Currency balanceCurrency) {
    return balance.get(balanceCurrency.ordinal());
  }
  public void renewBalance(Money newMoney){
    balance.replace(newMoney.getCurrency().ordinal(), newMoney);
  }
}
