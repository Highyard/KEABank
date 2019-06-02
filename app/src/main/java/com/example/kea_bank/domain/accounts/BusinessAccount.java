package com.example.kea_bank.domain.accounts;

import com.example.kea_bank.domain.users.User;

public class BusinessAccount extends Account{

    public BusinessAccount(Double balance) {
        this.balance = balance;
        this.activated = false;
    }


    @Override
    public void increment(User userAccount) {

    }

    @Override
    public void send(User sender, User receiver, Double amount) {

    }
}
