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

  public void mainProcess(String customerId, Money inputMoney, String todo) {
    checkInvalidInput(inputMoney);

    Customer customer = getCustomerData(customerId);

    switch (todo) {
      case "DEPOSIT":
        depositProcess(customer, inputMoney);
        break;
      case "WITHDRAW":
        withdrawProcess(customer, inputMoney);
        break;
      default:
        break;
    }
  }

  public void mainProcess(Money inputMoney, Currency wantToChange) {
    checkInvalidInput(inputMoney);

    exchangeProcess(inputMoney, wantToChange);
  }

  public void depositProcess(Customer customer, Money inputMoney) {
    Currency currency = inputMoneyTypeCheck(inputMoney);

    Money originalBalance = customer.getBalance(currency);

    deposit(originalBalance, inputMoney);
  }

  public double deposit(Money originalBalance, Money inputMoney) {
    return originalBalance.getAmount() + inputMoney.getAmount();
  }

  public Currency inputMoneyTypeCheck(Money inputMoney) {
    return inputMoney.getCurrency();
  }

  private void withdrawProcess(Customer customer, Money inputMoney) {
    Currency currency = inputMoneyTypeCheck(inputMoney);

    Money originalBalance = customer.getBalance(currency);

    withdraw(originalBalance, inputMoney);
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
    Money resultMoney;
    if (inputMoney.getCurrency() == Currency.WON) {

    }
  }
  public Money exchangeKo2Dollar(Money inputMoney) {

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
