package com.aston.audioHosting.users;

import com.aston.audioHosting.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    private final DBUserStorage dbUserStorage;

    @Autowired
    public UserService(DBUserStorage dbUserStorage) {
        this.dbUserStorage = dbUserStorage;
    }

    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        User createdUser = dbUserStorage.createUser(user);
        log.info("Пользователь был добавлен");
        return createdUser;
    }

    public User getUser(String login) {
        User user = dbUserStorage.getUser(login);
        if (!Objects.equals(user.getLogin(), login)) {
            throw new ObjectNotFoundException("Пользователь c login: " + login + " не найден");
        }
        return user;
    }

    public Collection<User> getAllUsers() {
        Collection<User> users = dbUserStorage.getAllUsers();
        log.info("Получен полный список пользователей");
        return users;
    }

    public User updateUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        User updatedUser = dbUserStorage.updateUser(user);
        log.info("Пользователь c login: {} был изменен", user.getLogin());
        return updatedUser;
    }

    public User deleteUser(String login) {
        User user = dbUserStorage.deleteUser(login);
        if (!Objects.equals(user.getLogin(), login)) {
            throw new ObjectNotFoundException("Пользователь c login: " + login + " не найден");
        }
        log.info("Пользователь c login: {} был удален", login);
        return user;
    }

}
