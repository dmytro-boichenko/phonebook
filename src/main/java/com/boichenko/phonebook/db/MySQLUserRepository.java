package com.boichenko.phonebook.db;

import com.boichenko.phonebook.model.User;

public class MySQLUserRepository implements UserRepository {

    @Override
    public boolean authenticateUser(User user) {
        return false;
    }

    @Override
    public boolean registerUser(User user) {
        return false;
    }

}
