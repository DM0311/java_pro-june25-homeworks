package ru.otus.java.pro.dao;

import java.util.Optional;
import ru.otus.java.pro.model.User;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);
}
