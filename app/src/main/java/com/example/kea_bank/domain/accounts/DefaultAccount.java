package com.example.kea_bank.domain.accounts;

import com.example.kea_bank.domain.users.User;

public class DefaultAccount extends Account{

    public DefaultAccount(Double balance) {
        this.balance = balance;
        this.activated = true;
    }

    @Override
    public void increment(User userAccount) {

    }

    @Override
    public void send(User sender, User receiver, Double amount) {

    }
}
