package com.boichenko.phonebook.db;

import com.boichenko.phonebook.model.Contact;
import com.boichenko.phonebook.model.Phone;
import com.boichenko.phonebook.model.User;

import java.util.List;

public interface ContactsRepository {

    List<Contact> getContacts(User user);

    int createContact(User user, Contact contact);

    void updateContact(User user, Contact contact);

    void deleteContact(User user, Contact contact);

    void addPhoneToContact(Contact contact, Phone phone);

    List<Phone> getPhonesForContact(Contact contact);

}
