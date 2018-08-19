package com.boichenko.phonebook.service;

import com.boichenko.phonebook.model.User;

public interface AuthService {

    boolean authenticate(User user);

    boolean register(User user);

}
