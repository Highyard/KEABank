package com.example.kea_bank.domain.accounts;

import com.example.kea_bank.domain.users.User;

public class BudgetAccount extends Account {

    public BudgetAccount(Double balance) {
        this.balance = balance;
        this.activated = true;
    }


    @Override
    public void increment(User sender, User receiver) {

    }
}
