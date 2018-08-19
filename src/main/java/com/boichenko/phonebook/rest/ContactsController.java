package com.boichenko.phonebook.rest;

import com.boichenko.phonebook.model.Contact;
import com.boichenko.phonebook.model.Phone;
import com.boichenko.phonebook.model.User;
import com.boichenko.phonebook.service.AuthService;
import com.boichenko.phonebook.service.ContactsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    private final AuthService authService;
    private final ContactsService contactsService;

    public ContactsController(AuthService authService, ContactsService contactsService) {
        this.authService = authService;
        this.contactsService = contactsService;
    }

    @GetMapping
    public List<Contact> getContacts(@RequestHeader("Authorization") String token) {
        User user = authService.authenticateByToken(token);
        return contactsService.getContacts(user);
    }

    @GetMapping("/{contactId}")
    public Contact getContact(@PathVariable int contactId,
                              @RequestHeader("Authorization") String token) {
        User user = authService.authenticateByToken(token);
        return contactsService.getContact(user, contactId);
    }

    @PostMapping("/create")
    public Map<String, Object> createContact(@RequestBody Contact contact,
                                             @RequestHeader("Authorization") String token) {
        User user = authService.authenticateByToken(token);
        int contactId = contactsService.createContact(user, contact);
        return Collections.singletonMap("contactId", contactId);
    }

    @PutMapping("/update")
    public Map<String, Object> updateContact(@RequestBody Contact contact,
                                             @RequestHeader("Authorization") String token) {
        User user = authService.authenticateByToken(token);
        contactsService.updateContact(user, contact);
        return CommonHelper.SUCCESS_RESPONSE;
    }

    @DeleteMapping("/delete")
    public Map<String, Object> deleteContact(@RequestBody Contact contact,
                                             @RequestHeader("Authorization") String token) {
        User user = authService.authenticateByToken(token);
        contactsService.deleteContact(user, contact);
        return CommonHelper.SUCCESS_RESPONSE;
    }

    @PostMapping("/{contactId}/phone")
    public Map<String, Object> addPhone(@RequestBody Phone phone,
                                        @PathVariable int contactId,
                                        @RequestHeader("Authorization") String token) {
        User user = authService.authenticateByToken(token);
        contactsService.addPhoneToContact(user, contactId, phone);
        return CommonHelper.SUCCESS_RESPONSE;
    }


}
