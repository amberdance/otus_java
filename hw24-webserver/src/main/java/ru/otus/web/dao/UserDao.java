package ru.otus.web.dao;


import ru.otus.web.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);
}
