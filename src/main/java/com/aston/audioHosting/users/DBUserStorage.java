package com.aston.audioHosting.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collection;
import java.util.Objects;


@Slf4j
@Repository
public class DBUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedStatement;
                }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User getUser(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::makeUser, login);
        } catch (EmptyResultDataAccessException ex) {
            System.out.println("Пользователь с login: " + login + " не найден");
            return null;
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return null;
    }

    @Override
    public User deleteUser(String login) {
        String sql = "DELETE FROM users WHERE login = ?";
        User user = getUser(login);
        jdbcTemplate.update(sql, login);
        return user;
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
    }
}
