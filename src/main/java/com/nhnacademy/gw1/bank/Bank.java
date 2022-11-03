package com.nhnacademy.gw1.bank;

import com.nhnacademy.gw1.money.Money;

public class Bank {

  public void BankProcess(String customerId, Money inputMoney){

  }

  public double deposit(Money originalBalance, Money inputMoney) {
    return originalBalance.addMoney(inputMoney);
  }
}
