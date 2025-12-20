package ru.otus.java.pro.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.java.pro.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {}
