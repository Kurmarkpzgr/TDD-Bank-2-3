package com.nhnacademy.gw1.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.gw1.bank.Bank;
import com.nhnacademy.gw1.money.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CustomerTest {
  Customer customer;


  @BeforeEach
  void setUp() {
  }

  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void customer_checkGetter(String currencyType) {
    String customerId = "customerOne";
    String password = "validPw";
    Currency currency = Currency.valueOf(currencyType);

    Customer customer = new Customer(customerId, password);
    assertThat(customer.getBalance(currency).getCurrency()).isEqualTo(currency);
  }

}