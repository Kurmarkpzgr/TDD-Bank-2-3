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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
class BankTest {

  final double EXCHANGE_FEE = 0.015;

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
    Money inputMoney = new Money(currencyType, -1000);

    assertThatThrownBy(() -> bank.checkInvalidInput(inputMoney))
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
        .isInstanceOf(InvalidWithdrawInputException.class)
        .hasMessageContainingAll(inputMoney.getAmount() + "", " is over your balance");
  }

  @Test
  void bank_invalidWithdrawInput_thenThrowInvalidWithdrawInputException() {
    Currency currencyType = Currency.WON;
    Money inputMoney = new Money(currencyType, 1000);

    when(repository.findById(customer.getCustomerId())).thenReturn(customer);
    Money customerBalance = customer.getBalance(inputMoney.getCurrency());

    assertThatThrownBy(() -> bank.withdraw(customerBalance, inputMoney))
        .isInstanceOf(InvalidWithdrawInputException.class)
        .hasMessageContainingAll(inputMoney.getAmount() + " is over your balance");
  }

  //동일 국적 화폐로 환전 시도
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_exchangeEqualCurrency_thenThrowEqualCurrencyException(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    Money inputMoney = new Money(currency, 1000);

    Currency wantToChange = currency;

    assertThatThrownBy(() -> bank.exchangeProcess(inputMoney, wantToChange))
        .isInstanceOf(EqualCurrencyException.class)
        .hasMessageContaining("You are trying to exchange same " + currency);

  }

  //exchangeTest
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_exchangeFee(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    Money inputMoney = new Money(currency, 1000);

    double result = bank.exchangeFee(inputMoney);

    assertThat(result).isEqualTo(inputMoney.getAmount() * EXCHANGE_FEE);
  }

  @Test
  void bank_exchangeDollarToWon() {
    Money inputMoney = new Money(Currency.DOLLAR, 5.25);

    Money result = bank.exchangeDollarToWon(inputMoney);

    assertThat(result.getCurrency()).isEqualTo(Currency.WON);
    assertThat(result.getAmount()).isEqualTo(inputMoney.getAmount() * 1000);
  }

  @Test
  void bank_exchangeWonToDollar() {
    Money inputMoney = new Money(Currency.WON, 4321);

    Money result = bank.exchangeWonToDollar(inputMoney);

    double exchange = inputMoney.getAmount()/1000;

    assertThat(result.getCurrency()).isEqualTo(Currency.DOLLAR);
    assertThat(result.getAmount()).isEqualTo((double)Math.round(exchange * 100) / 100);
    log.info(result.getAmount() +"");
  }

 @Test
  void exchange_TotalDollarToWonTest(){
   Money inputMoney = new Money(Currency.DOLLAR, 4000);

   double payedAmount = bank.exchangeFee(inputMoney);

   Money result =bank.exchangeProcess(inputMoney, Currency.WON);

   assertThat(result.getCurrency()).isEqualTo(Currency.WON);
   assertThat(result.getAmount()).isEqualTo(payedAmount*1000);
 }

}