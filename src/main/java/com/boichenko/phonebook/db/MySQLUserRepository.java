package com.boichenko.phonebook.db;

import com.boichenko.phonebook.exception.NotFoundException;
import com.boichenko.phonebook.model.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MySQLUserRepository implements UserRepository {

    private JdbcTemplate jdbcTemplate;

    public MySQLUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(String userName) {
        try {
            return jdbcTemplate.queryForObject("SELECT user_id, user_name, password FROM user WHERE user_name = ?",
                    (rs, rowNum) -> {
                        User user = new User();
                        user.setId(rs.getInt("user_id"));
                        user.setUserName(rs.getString("user_name"));
                        user.setPassword(rs.getString("password"));
                        return user;
                    });
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("User '" + userName + "' wasn't found");
        }
    }

    @Override
    public void registerUser(User user) {
        jdbcTemplate.update("INSERT INTO user (user_name, password) VALUES (?, ?)");
    }

}
