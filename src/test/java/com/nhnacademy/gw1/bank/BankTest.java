package com.nhnacademy.gw1.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.gw1.customer.Customer;
import com.nhnacademy.gw1.customer.CustomerRepository;
import com.nhnacademy.gw1.exception.InvalidInputException;
import com.nhnacademy.gw1.exception.InvalidWithdrawInputException;
import com.nhnacademy.gw1.money.Currency;
import com.nhnacademy.gw1.money.Money;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class BankTest {

  //SUT
  Bank bank;
  //DOC
  CustomerRepository repository;
  Customer customer;


  @BeforeEach
  void setUp() {
    repository = mock(CustomerRepository.class);
    bank = new Bank(repository);

    String customerId = "customerOne";
    String password = "validPw";
    customer = new Customer(customerId, password);
  }


  //입금 성공
  @Test
  void bank_wonDepositSuccess() {
    Currency currency = Currency.WON;
    Money inputMoney = new Money(currency, 1000);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getBalance(inputMoney.getCurrency());

    double result = bank.deposit(customerBalance, inputMoney);
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(customerBalance.getAmount() + inputMoney.getAmount());
  }

  //달러 입금
  @Test
  void bank_dollarDepositSuccess() {
    Currency currency = Currency.DOLLAR;
    Money inputMoney = new Money(currency, 4);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getBalance(inputMoney.getCurrency());

    double result = bank.deposit(customerBalance, inputMoney);
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(customerBalance.getAmount() + inputMoney.getAmount());
  }


  //음수 여부 체크
  @Test
  void bank_depositFailure_thenThrowInvalidInputException() {
    Currency currency = Currency.WON;
    Money inputMoney = new Money(currency, -1000);

    assertThatThrownBy(() -> bank.inputMoneyValidCheck(inputMoney.getAmount()))
        .isInstanceOf(InvalidInputException.class)
        .hasMessageContainingAll("Invalid money input Exception" + inputMoney.getAmount());
  }

  //금액 비교 성공 실패
  @Test
  void bank_widthdrawInputError_thenTrowInvalidWithdrawInputException() {
    Currency currency = Currency.WON;
    Money inputMoney = new Money(currency, 1000);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getBalance(inputMoney.getCurrency());

    assertThatThrownBy(() -> bank.withdraw(customerBalance,inputMoney))
        .isInstanceOf(InvalidWithdrawInputException.class)
        .hasMessageContainingAll(inputMoney.getAmount() + " is over your balance");

  }

  //현재 능력 바깥
//  @Test
//  void bank_invalidCurrency_thenThrowInvalidCurrencyException() {
//    Currency currency = Currency.valueOf("YEN");
//    Money inputMoney = new Money(currency, 1000);
//
//        assertThatThrownBy(() -> bank.inputMoneyTypeCheck(inputMoney))
//            .isInstanceOf(IllegalArgumentException.class);
//  }

  //음수 여부 확인
  @Test
  void bank_moneyPositiveTest() {

  }

}