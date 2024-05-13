package com.aston.audioHosting.users;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    User getUser(String login);

    Collection<User> getAllUsers();

    User updateUser(User user);

    User deleteUser(String login);
}
