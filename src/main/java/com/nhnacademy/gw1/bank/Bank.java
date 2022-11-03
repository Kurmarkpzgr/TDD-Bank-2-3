package com.nhnacademy.gw1.bank;

import com.nhnacademy.gw1.customer.Customer;
import com.nhnacademy.gw1.customer.CustomerRepository;
import com.nhnacademy.gw1.exception.CustomerNotFoundException;
import com.nhnacademy.gw1.exception.InvalidInputException;
import com.nhnacademy.gw1.money.Currency;
import com.nhnacademy.gw1.money.Money;

public class Bank {
  private final CustomerRepository customerRepository;
  
  private final int DEPOSIT = 0;
  private final int WITHDRAW = 1;
  private final int EXCHANGE = 2;

  public Bank(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public void mainProcess(String customerId, Money inputMoney, int todo) {
    checkInvalidInput(inputMoney);

    Customer customer = getCustomerData(customerId);

    switch (todo) {
      case DEPOSIT:
        depositProcess(customer, inputMoney);
        break;
      case WITHDRAW:
        withdrawProcess(customer, inputMoney);
        break;
      case EXCHANGE:
        exchangeProcess(inputMoney);
        break;
    }
  }
  public void depositProcess(Customer customer, Money inputMoney){

    Currency currency = inputMoneyTypeCheck(inputMoney);


    Money originalBalance = customer.getBalance(currency);
    return originalBalance.getAmount() + inputMoney.getAmount();
  }

  public Currency inputMoneyTypeCheck(Money inputMoney) {
    return inputMoney.getCurrency();
  }
  public void checkInvalidInput(Money inputMoney) {
    if(inputMoney.getAmount() < 0) {
      throw new InvalidInputException(inputMoney);
    }
  }
  public Customer getCustomerData(String customerId) {
    Customer customer;

    if((customer = customerRepository.findById(customerId)) == null) {
      throw new CustomerNotFoundException(customerId);
    }
    return customer;
  }
}
