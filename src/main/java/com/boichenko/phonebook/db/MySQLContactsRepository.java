package com.boichenko.phonebook.db;

import com.boichenko.phonebook.model.Contact;
import com.boichenko.phonebook.model.Phone;
import com.boichenko.phonebook.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public int createContact(User user, Contact contact) {
        jdbcTemplate.update("INSERT INTO contact (user_id, first_name, last_name) VALUES (?, ?, ?)",
                user.getId(), contact.getFirstName(), contact.getLastName());
        Integer contactId = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);

        if (contactId == null || contactId <= 0)
            throw new RuntimeException("Contact wasn't added");

        contact.setId(contactId);
        contact.getPhones().forEach(p -> addPhoneToContact(contact, p));
        return contactId;
    }

    @Override
    public void updateContact(User user, Contact contact) {
        jdbcTemplate.update("UPDATE contact SET first_name = ?, last_name = ? WHERE contact_id = ?",
                contact.getFirstName(), contact.getLastName(), contact.getId());
    }

    @Override
    public void deleteContact(User user, Contact contact) {
        jdbcTemplate.update("DELETE FROM contact WHERE contact_id = ?", contact.getId());
    }

    @Override
    public void addPhoneToContact(Contact contact, Phone phone) {
        jdbcTemplate.update("INSERT INTO phone (contact_id, phone) VALUES (?, ?)",
                contact.getId(), phone.getPhone());
    }

    @Override
    public List<Phone> getPhonesForContact(Contact contact) {
        return jdbcTemplate.query("SELECT phone_id, contact_id, phone FROM phone WHERE contact_id = ?", new PhoneRowMapper());
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
