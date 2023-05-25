package ru.otus.service;

import ru.otus.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    void createuser(User user);
}
