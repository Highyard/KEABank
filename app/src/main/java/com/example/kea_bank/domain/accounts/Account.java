package com.example.kea_bank.domain.accounts;

import com.example.kea_bank.domain.users.User;

public abstract class Account implements Interest{

    boolean activated   = false;
    Double balance      = null;
    String userId       = null;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    void move(User sender, User receiver, Double amount) {

    }


}
