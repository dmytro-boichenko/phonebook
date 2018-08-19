package com.boichenko.phonebook.db;

import com.boichenko.phonebook.model.User;

public interface UserRepository {

    User getUser(String userName);

    void registerUser(User user);
}
