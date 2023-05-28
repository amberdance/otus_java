package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
