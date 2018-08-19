package com.boichenko.phonebook.db;

import com.boichenko.phonebook.model.User;

public interface UserRepository {

    boolean authenticateUser(User user);

    boolean registerUser(User user);
}
