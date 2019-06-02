package com.example.kea_bank.domain.accounts;

import com.example.kea_bank.domain.users.User;

public interface Interest {

    void increment(User userAccount);

    void send(User sender, User receiver, Double amount);
}
