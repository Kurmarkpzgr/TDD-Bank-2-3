package com.nhnacademy.gw1.money;

import com.nhnacademy.gw1.money.Money;

public class Won implements Money {
  private double money;

  public Won(double money) {
    this.money = money;
  }

  public double getMoney(){
    return this.money;
  }
  @Override
  public double addMoney(Money inputMoney) {
    return  this.money + inputMoney.getMoney();
  }

  @Override
  public double surMoney(Money outputMoney) {
    return this.money - outputMoney.getMoney();
  }
}
