package com.nhnacademy.gw1.bank;

import static org.mockito.Mockito.mock;

import com.nhnacademy.gw1.customer.Customer;
import com.nhnacademy.gw1.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankTest {

  //SUT
  Bank bank;
  //DOC
  CustomerRepository repository;
  Customer customer;


  @BeforeEach
  void setUp() {
    repository = mock(CustomerRepository.class);
    bank = new Bank();

    String customerId = "customerOne";
    String password = "validPw";
    customer = new Customer(customerId, password);
  }


  //입금 성공 실패
  @Test
  void bank_depositSuccess() {
    long inputMoney = 1000L;

    bank.deposit(inputMoney);

  }

  @Test
  void bank_depositFailure() {

  }

  //금액 비교 성공 실패
  @Test
  void bank_compareSuccess() {

  }

  @Test
  void bank_compareFailure() {

  }

  //음수 여부 확인
  @Test
  void bank_moneyPositiveTest() {

  }

}