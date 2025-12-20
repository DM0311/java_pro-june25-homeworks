package ru.otus.java.pro.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.java.pro.model.Phone;

public interface PhoneRepository extends CrudRepository<Phone, Long> {}
