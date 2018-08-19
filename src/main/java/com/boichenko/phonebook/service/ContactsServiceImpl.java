package com.boichenko.phonebook.service;

import com.boichenko.phonebook.db.ContactsRepository;
import com.boichenko.phonebook.model.Contact;
import com.boichenko.phonebook.model.Phone;
import com.boichenko.phonebook.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {

    private final ContactsRepository contactsRepository;

    public ContactsServiceImpl(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    @Override
    public List<Contact> getContacts(User user) {
        return contactsRepository.getContacts(user);
    }

    @Override
    public Contact getContact(User user, int contactId) {
        return contactsRepository.getContact(user, contactId);
    }

    @Override
    public int createContact(User user, Contact contact) {
        return contactsRepository.createContact(user, contact);
    }

    @Override
    public void updateContact(User user, Contact contact) {
        contactsRepository.updateContact(user, contact);
    }

    @Override
    public void deleteContact(User user, Contact contact) {
        contactsRepository.deleteContact(user, contact);
    }

    @Override
    public void addPhoneToContact(User user, int contactId, Phone phone) {
        Contact contact = contactsRepository.getContact(user, contactId);
        contactsRepository.addPhoneToContact(user, contact, phone);
    }
}
