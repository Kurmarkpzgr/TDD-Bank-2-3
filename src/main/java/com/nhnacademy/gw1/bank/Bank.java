package com.nhnacademy.gw1.bank;

import com.nhnacademy.gw1.customer.Customer;
import com.nhnacademy.gw1.customer.CustomerRepository;
import com.nhnacademy.gw1.exception.CustomerNotFoundException;
import com.nhnacademy.gw1.exception.EqualCurrencyException;
import com.nhnacademy.gw1.exception.InvalidInputException;
import com.nhnacademy.gw1.exception.InvalidWithdrawInputException;
import com.nhnacademy.gw1.money.Currency;
import com.nhnacademy.gw1.money.Money;

public class Bank {

  private final CustomerRepository customerRepository;

  public Bank(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Money mainProcess(String customerId, Money inputMoney, String todo) {
    checkInvalidInput(inputMoney);

    Customer customer = getCustomerData(customerId);

    Money money = new Money(Currency.WON, 100);
    switch (todo) {
      case "DEPOSIT":
        money = depositProcess(customer, inputMoney);
        break;
      case "WITHDRAW":
        money = withdrawProcess(customer, inputMoney);
        break;
    }
    customer.renewBalance(money);
    return money;
  }

  public Money mainProcess(Money inputMoney, Currency wantToChange) {
    checkInvalidInput(inputMoney);

    Money exchangedMoney = exchangeProcess(inputMoney, wantToChange);

    return exchangedMoney;
  }

  public Money depositProcess(Customer customer, Money inputMoney) {
    Currency currency = inputMoneyTypeCheck(inputMoney);

    Money originalBalance = customer.getBalance(currency);

    double deposited = deposit(originalBalance, inputMoney);
    Money resultMoney = new Money(inputMoney.getCurrency(), deposited);

    return resultMoney;
  }

  public double deposit(Money originalBalance, Money inputMoney) {
    return originalBalance.getAmount() + inputMoney.getAmount();
  }

  public Currency inputMoneyTypeCheck(Money inputMoney) {
    return inputMoney.getCurrency();
  }

  public Money withdrawProcess(Customer customer, Money inputMoney) {
    Currency currency = inputMoneyTypeCheck(inputMoney);

    Money originalBalance = customer.getBalance(currency);

    double deposited = withdraw(originalBalance, inputMoney);
    Money resultMoney = new Money(inputMoney.getCurrency(), deposited);

    return resultMoney;
  }

  public double withdraw(Money customerBalance, Money inputMoney) {
    if (customerBalance.getAmount() < inputMoney.getAmount()) {
      throw new InvalidWithdrawInputException(inputMoney.getAmount());
    }
    return customerBalance.getAmount() - inputMoney.getAmount();
  }

  public Money exchangeProcess(Money inputMoney, Currency exchangeToThis) {
    if (inputMoney.getCurrency() == exchangeToThis) {
      throw new EqualCurrencyException(inputMoney.getCurrency());
    }
    inputMoney = new Money(inputMoney.getCurrency(),
            inputMoney.getAmount() - exchangeFee(inputMoney));

    if ((inputMoney.getCurrency() == Currency.DOLLAR && exchangeToThis == Currency.EURO) || (
            inputMoney.getCurrency() == Currency.EURO && exchangeToThis == Currency.DOLLAR)) {

      throw new InvalidExchangeRequestException(exchangeToThis);
    }
    Money exchangedMoney = null;
    if (inputMoney.getCurrency() == Currency.WON) {
      if (exchangeToThis == Currency.DOLLAR) {
        exchangedMoney = exchangeWonToDollar(inputMoney);
      } else {
        exchangedMoney = exchangeWonToEuro(inputMoney);
      }
    }
    else if(inputMoney.getCurrency() == Currency.DOLLAR) {
      exchangedMoney = exchangeDollarToWon(inputMoney);
    }
    else if(inputMoney.getCurrency() == Currency.EURO) {
      exchangedMoney = exchangeEuroToWon(inputMoney);
    }




    return exchangedMoney;
  }



  public Money exchangeWonToEuro(Money inputMoney) {
    Currency currency = Currency.EURO;
    double exchangedPayload = inputMoney.getAmount()/1300;
    double rounded =  (double)Math.round(exchangedPayload * 100) / 100;

    Money exchangedMoney = new Money(currency, rounded);
    return exchangedMoney;
  }
  public Money exchangeEuroToWon(Money inputMoney) {
    Currency currency = Currency.WON;
    double exchangedPayload = inputMoney.getAmount() * 1300;
    double rounded =  (double)Math.round(exchangedPayload * 100) / 100;

    Money exchangedMoney = new Money(currency, rounded);
    return exchangedMoney;
  }

  public Money exchangeWonToDollar(Money inputMoney) {
    Currency currency = Currency.DOLLAR;
    double exchangedPayload = inputMoney.getAmount()/1000;
    double rounded =  (double)Math.round(exchangedPayload * 100) / 100;

    Money exchangedMoney = new Money(currency, rounded);
    return exchangedMoney;
  }

  public Money exchangeDollarToWon(Money inputMoney) {
    Currency currency = Currency.WON;
    double changedMoney = inputMoney.getAmount() * 1000;
    double rounded = Math.round(changedMoney/10)*10;

    Money exchangedMoney = new Money(currency, rounded);
    return exchangedMoney;
  }

  public double exchangeFee(Money inputMoney) {
    double exchangeFee = 0.015;

    return inputMoney.getAmount() * exchangeFee;
  }
  public void checkInvalidInput(Money inputMoney) {
    if (inputMoney.getAmount() < 0) {
      throw new InvalidInputException(inputMoney.getAmount());
    }
  }

  public Customer getCustomerData(String customerId) {
    Customer customer;

    if ((customer = customerRepository.findById(customerId)) == null) {
      throw new CustomerNotFoundException(customerId);
    }
    return customer;
  }
}
