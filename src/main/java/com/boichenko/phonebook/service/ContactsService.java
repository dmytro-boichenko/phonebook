package com.boichenko.phonebook.service;

import com.boichenko.phonebook.model.Contact;
import com.boichenko.phonebook.model.Phone;
import com.boichenko.phonebook.model.User;

import java.util.List;

public interface ContactsService {

    List<Contact> getContacts(User user);

    Contact getContact(User user, int contactId);

    int createContact(User user, Contact contact);

    void updateContact(User user, Contact contact);

    void deleteContact(User user, Contact contact);

    void addPhoneToContact(User user, int contactId, Phone phone);

}
