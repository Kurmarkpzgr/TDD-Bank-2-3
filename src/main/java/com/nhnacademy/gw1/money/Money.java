package com.nhnacademy.gw1.money;

public interface Money {
  double getMoney();

  double addMoney(Money inputMoney);
  double surMoney(Money outputMoney);
}
