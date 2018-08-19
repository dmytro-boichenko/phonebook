package com.boichenko.phonebook.db;

import com.boichenko.phonebook.exception.NotFoundException;
import com.boichenko.phonebook.model.Contact;
import com.boichenko.phonebook.model.Phone;
import com.boichenko.phonebook.model.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Repository
public class MySQLContactsRepository implements ContactsRepository {

    private JdbcTemplate jdbcTemplate;

    public MySQLContactsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Contact> getContacts(User user) {
        List<Contact> contacts = jdbcTemplate.query("SELECT contact_id, user_id, first_name, last_name " +
                "FROM contact WHERE user_id = ?", new ContactRowMapper());
        contacts.forEach(c -> {
            List<Phone> phones = this.getPhonesForContact(c);
            c.setPhones(phones);
        });
        return contacts;
    }

    @Override
    public Contact getContact(User user, int contactId) {
        try {
            Object[] args = {user.getId(), contactId};
            return jdbcTemplate.queryForObject("SELECT contact_id, user_id, first_name, last_name " +
                    "FROM contact WHERE user_id = ? AND contact_id = ?", args, new ContactRowMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Contact not found");
        }
    }

    @Override
    @Transactional
    public int createContact(User user, Contact contact) {
        jdbcTemplate.update("INSERT INTO contact (user_id, first_name, last_name) VALUES (?, ?, ?)",
                user.getId(), contact.getFirstName(), contact.getLastName());
        Integer contactId = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);

        if (contactId == null || contactId <= 0)
            throw new RuntimeException("Contact wasn't added");

        contact.setId(contactId);
        contact.getPhones().forEach(p -> addPhoneToContact(user, contact, p));
        return contactId;
    }

    @Override
    public void updateContact(User user, Contact contact) {
        Contact dbContact = getContact(user, contact.getId());
        String firstName = StringUtils.isEmpty(contact.getFirstName()) ? dbContact.getFirstName() : contact.getFirstName();
        String lastName = StringUtils.isEmpty(contact.getLastName()) ? dbContact.getLastName() : contact.getLastName();
        jdbcTemplate.update("UPDATE contact SET first_name = ?, last_name = ? WHERE contact_id = ? AND user_id = ?",
                firstName, lastName, contact.getId(), user.getId());
    }

    @Override
    public void deleteContact(User user, Contact contact) {
        jdbcTemplate.update("DELETE FROM contact WHERE contact_id = ? AND user_id = ?", contact.getId(), user.getId());
    }

    @Override
    public void addPhoneToContact(User user, Contact contact, Phone phone) {
        Contact dbContact = getContact(user, contact.getId());
        jdbcTemplate.update("INSERT INTO phone (contact_id, phone) VALUES (?, ?)",
                dbContact.getId(), phone.getPhone());
    }

    private List<Phone> getPhonesForContact(Contact contact) {
        Object[] args = {contact.getId()};
        return jdbcTemplate.query("SELECT phone_id, contact_id, phone FROM phone WHERE contact_id = ?", args, new PhoneRowMapper());
    }


    private static class ContactRowMapper implements RowMapper<Contact> {

        @Override
        public Contact mapRow(ResultSet rs, int i) throws SQLException {
            Contact contact = new Contact();
            contact.setId(rs.getInt("contact_id"));
            contact.setUserId(rs.getInt("user_id"));
            contact.setFirstName(rs.getString("first_name"));
            contact.setLastName(rs.getString("last_name"));
            return contact;
        }
    }

    private static class PhoneRowMapper implements RowMapper<Phone> {
        @Override
        public Phone mapRow(ResultSet rs, int rowNum) throws SQLException {
            Phone phone = new Phone();
            phone.setId(rs.getInt("phone_id"));
            phone.setContactId(rs.getInt("contact_id"));
            phone.setPhone(rs.getString("phone"));
            return phone;
        }
    }
}
