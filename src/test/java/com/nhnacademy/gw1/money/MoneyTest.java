package com.nhnacademy.gw1.money;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    //SUT
    Money money;

    @BeforeEach
    void setUp(){
        money = new Money(Currency.WON, 2000);
    }

    @Test
    void moneyEqualTest(){
        Money expectMoney = new Money(Currency.WON, 2000);

        assertThat(expectMoney.equals(money)).isEqualTo(true);
    }
}