package com.nhnacademy.gw1.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
  @Test
  void bank_invalidInput_thenThrowInvalidInputException() {
    Currency currencyType = Currency.WON;
    Money inputMoney = new Money(currencyType, -1000);

    assertThatThrownBy(() -> bank.checkInvalidInput(inputMoney))
            .isInstanceOf(InvalidInputException.class)
            .hasMessageContainingAll("Invalid money input Exception", inputMoney.getAmount() + "");
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

    log.info(result +"");
  }
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_depositProcessSuccess(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    Money inputMoney = new Money(currency, 100);

    customer.renewBalance(new Money(currency, 1000));
    when(repository.findById(customer.getCustomerId())).thenReturn(customer);

    Money customerBalance = customer.getBalance(currency);

    Money result = bank.depositProcess(customer, inputMoney);
    assertThat(result).isNotNull();
    assertThat(result.getAmount()).isEqualTo(customerBalance.getAmount() + inputMoney.getAmount());

    log.info(result +"");
  }



  //출금 에러: 잔고보다 큰 출금 요청
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
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_withdrawSuccess(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    Money inputMoney = new Money(currency, 100);

    customer.renewBalance(new Money(currency, 1000));
    when(repository.findById(customer.getCustomerId())).thenReturn(customer);

    Money customerBalance = customer.getBalance(currency);

    double result = bank.withdraw(customerBalance, inputMoney);
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(customerBalance.getAmount() - inputMoney.getAmount());

    log.info(result +"");
  }
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_withdrawProcessSuccess(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    Money inputMoney = new Money(currency, 100);

    customer.renewBalance(new Money(currency, 1000));
    when(repository.findById(customer.getCustomerId())).thenReturn(customer);

    Money customerBalance = customer.getBalance(currency);

    Money result = bank.withdrawProcess(customer, inputMoney);
    assertThat(result).isNotNull();
    assertThat(result.getAmount()).isEqualTo(customerBalance.getAmount() - inputMoney.getAmount());

    log.info(result +"");
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

  @ParameterizedTest
  @ValueSource(doubles = {431, 2352, 1000, 435000})
  void bank_exchangeWonToDollar(double amount) {
    Money inputMoney = new Money(Currency.WON, amount);

    Money result = bank.exchangeWonToDollar(inputMoney);

    double exchange = inputMoney.getAmount()/1000;

    assertThat(result.getCurrency()).isEqualTo(Currency.DOLLAR);
    assertThat(result.getAmount()).isEqualTo((double)Math.round(exchange * 100) / 100);
    log.info(result.getAmount() +"");
  }

 @Test
  void exchange_TotalDollarToWonTest(){
   Money inputMoney = new Money(Currency.DOLLAR, 0.26);

   double payedAmount = inputMoney.getAmount() - bank.exchangeFee(inputMoney);

   Money result =bank.exchangeProcess(inputMoney, Currency.WON);

   assertThat(result.getCurrency()).isEqualTo(Currency.WON);
   assertThat(result.getAmount()).isEqualTo(Math.round(payedAmount*1000/10)*10);
   log.info(result.toString());
 }
  @ParameterizedTest
  @ValueSource(doubles = {4320, 23520, 1000, 435000})
  void exchange_TotalWonToDollarTest(double amount){
   Money inputMoney = new Money(Currency.WON, amount);

   double payedAmount = inputMoney.getAmount() - bank.exchangeFee(inputMoney);

   Money result =bank.exchangeProcess(inputMoney, Currency.DOLLAR);

   assertThat(result.getCurrency()).isEqualTo(Currency.DOLLAR);
   assertThat(result.getAmount()).isEqualTo((double)Math.round(payedAmount/1000 * 100) / 100);
 }
  @ParameterizedTest
  @ValueSource(strings = {"WON", "DOLLAR"})
  void bank_mainProcess_deposit_success(String currencyType) {
    Currency currency = Currency.valueOf(currencyType);
    String customerId = customer.getCustomerId();
    Money inputMoney = new Money(currency, 100);

    when(repository.findById(customerId)).thenReturn(customer);

    Money result = bank.mainProcess(customerId, inputMoney, "DEPOSIT");

    Money customerBalance = customer.getBalance(currency);

    assertThat(result.getAmount()).isEqualTo(customerBalance.getAmount());
  }
  @Test
  void bank_mainProcessWithdrawTest(){
    String customerId = customer.getCustomerId();
    Money inputMoney = new Money(Currency.DOLLAR, 4.25);


    when(repository.findById(customerId)).thenReturn(customer);
    Money customerMoney = new Money(inputMoney.getCurrency(), 5);
    customer.renewBalance(customerMoney);

    String todo = "WITHDRAW";
    bank.mainProcess(customerId, inputMoney, todo);

    Money expectedMoney = new Money(inputMoney.getCurrency(), customerMoney.getAmount()-inputMoney.getAmount());
    assertThat(customer.getBalance(inputMoney.getCurrency())).isEqualTo(expectedMoney);
  }

  @Test
  void bank_mainProcessExchangeDollarToWonTest() {
    Money inputMoney = new Money(Currency.DOLLAR, 4.25);
    Currency wantToChange = Currency.WON;

    Money result = bank.mainProcess(inputMoney, wantToChange);

    double payedAmount = inputMoney.getAmount() - bank.exchangeFee(inputMoney);

    assertThat(result.getAmount()).isEqualTo(Math.round(payedAmount*1000/10)*10);
    assertThat(result.getCurrency()).isEqualTo(wantToChange);
    log.info(result.getAmount()+"");
  }
  @Test
  void bank_mainProcessExchangeWonToDollarTest() {
    Money inputMoney = new Money(Currency.WON, 4000);
    Currency wantToChange = Currency.DOLLAR;
    Money result = bank.mainProcess(inputMoney, wantToChange);
    double payedAmount = inputMoney.getAmount() - bank.exchangeFee(inputMoney);

    assertThat(result.getCurrency()).isEqualTo(wantToChange);
    assertThat(result.getAmount()).isEqualTo((double) Math.round(payedAmount / 1000 * 100) / 100);
    log.info(result.getAmount()+"");
  }

  @Test
  void bank_exchangeEuroToWon() {
    Money inputMoney = new Money(Currency.EURO, 5.25);

    Money result = bank.exchangeEuroToWon(inputMoney);

    assertThat(result.getCurrency()).isEqualTo(Currency.WON);
    assertThat(result.getAmount()).isEqualTo(inputMoney.getAmount() * 1300);
  }

  @ParameterizedTest
  @ValueSource(doubles = {431, 2352, 1000, 435000})
  void bank_exchangeWonToEuro(double amount) {
    Money inputMoney = new Money(Currency.WON, amount);

    Money result = bank.exchangeWonToEuro(inputMoney);

    double exchange = inputMoney.getAmount()/1300;

    assertThat(result.getCurrency()).isEqualTo(Currency.EURO);
    assertThat(result.getAmount()).isEqualTo((double)Math.round(exchange * 100) / 100);
    log.info(result.getAmount() +"");
  }
}