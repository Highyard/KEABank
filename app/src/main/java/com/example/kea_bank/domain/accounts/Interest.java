package com.example.kea_bank.domain.accounts;

import com.example.kea_bank.domain.users.User;

public interface Interest {

    void increment(User sender, User receiver);

}
