package com.nhnacademy.gw1.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.gw1.customer.Customer;
import com.nhnacademy.gw1.customer.CustomerRepository;
import com.nhnacademy.gw1.exception.InvalidInputException;
import com.nhnacademy.gw1.money.Money;
import com.nhnacademy.gw1.money.Won;
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
    Money inputMoney =  new Won(1000);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.get();

    double result = bank.deposit(customerBalance, inputMoney);
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(customerBalance.addMoney(inputMoney));
  }

  @Test
  void bank_depositFailure() {
    Money inputMoney = new Won(1000L);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getWonBalance();

    assertThatThrownBy(()-> bank.deposit(customerBalance, inputMoney))
        .isInstanceOf(InvalidInputException.class)
        .hasMessageContainingAll("Invalid money input Exception", inputMoney.toString());
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