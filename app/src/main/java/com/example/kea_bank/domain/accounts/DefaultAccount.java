package com.example.kea_bank.domain.accounts;

public class DefaultAccount extends Account{

    public DefaultAccount(Double balance) {
        this.balance = balance;
        this.activated = true;
    }
}
