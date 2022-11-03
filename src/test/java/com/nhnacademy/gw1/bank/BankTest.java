package com.nhnacademy.gw1.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.gw1.customer.Customer;
import com.nhnacademy.gw1.customer.CustomerRepository;
import com.nhnacademy.gw1.exception.EqualCurrencyException;
import com.nhnacademy.gw1.exception.InvalidInputException;
import com.nhnacademy.gw1.exception.InvalidWithdrawInputException;
import com.nhnacademy.gw1.money.Currency;
import com.nhnacademy.gw1.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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


  //입금 성공 실패
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_depositSuccess(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    Money inputMoney = new Money(currency, 5.25);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);

    Money customerBalance = customer.getBalance(currency);

    double result = bank.deposit(customerBalance, inputMoney);
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(customerBalance.getAmount() + inputMoney.getAmount());
  }

  @Test
  void bank_invalidInput_thenThrowInvalidInputException() {
    Currency currencyType = Currency.WON;
    Money inputMoney = new Money(currencyType, 1000);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getBalance(inputMoney.getCurrency());

    assertThatThrownBy(() -> bank.mainProcess(customer.getCustomerId(), inputMoney, 1))
        .isInstanceOf(InvalidInputException.class)
        .hasMessageContainingAll("Invalid money input Exception", inputMoney.getAmount() + "");
  }

  //고객 정보 체크

  //통화 체크 : 불가능
  @Test
  void bank_depositFailure_NotEqualCurrency() {

  }

  //출금 에러: 잔고보다 큰 출금 요청
  @Test
  void bank_withdrawFailure() {
    Currency currencyType = Currency.WON;
    Money inputMoney = new Money(currencyType, 1000);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getBalance(inputMoney.getCurrency());

    assertThatThrownBy(() -> bank.withdraw(customerBalance, inputMoney))
        .isInstanceOf(InvalidInputException.class)
        .hasMessageContainingAll("Invalid money input Exception", inputMoney.getAmount() + "");
  }

  @Test
  void bank_invalidWithdrawInput_thenThrowInvalidWithdrawInputException() {
    Currency currencyType = Currency.WON;
    Money inputMoney = new Money(currencyType, 1000);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getBalance(inputMoney.getCurrency());

    assertThatThrownBy(() -> bank.mainProcess(customer.getCustomerId(), inputMoney, 1))
        .isInstanceOf(InvalidWithdrawInputException.class)
        .hasMessageContainingAll(inputMoney.getAmount() + " is over your balance");
  }

  //동일 국적 화폐로 환전 시도
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_exchangeEqualCurrenty_thenThrowEqualCurrencyException(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    Money inputMoney = new Money(currency, 1000);

    Currency wantToChange = currency;

    assertThatThrownBy(() -> bank.exchangeProcess(inputMoney, wantToChange)).isInstanceOf(
        EqualCurrencyException.class).hasMessageContaining("You are trying to exchange same " + currency );

  }

  //없는 화폐
  //능력 부족

}