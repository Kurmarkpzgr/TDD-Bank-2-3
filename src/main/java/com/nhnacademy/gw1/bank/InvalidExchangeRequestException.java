package com.nhnacademy.gw1.bank;

import com.nhnacademy.gw1.money.Currency;

public class InvalidExchangeRequestException extends RuntimeException {
    public InvalidExchangeRequestException(Currency exchangeToThis) {
        super("Korean Bank didn't exchange foreign currency to foreign currency. " + exchangeToThis.toString());
    }
}
