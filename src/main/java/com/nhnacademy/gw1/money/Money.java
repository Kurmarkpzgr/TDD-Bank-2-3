package com.nhnacademy.gw1.money;

public class Money {
    Currency currency;
    double amount;
    public Money(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public double getAmount(){
        return this.amount;
    }
    public Currency getCurrency() {return this.currency; }
}
