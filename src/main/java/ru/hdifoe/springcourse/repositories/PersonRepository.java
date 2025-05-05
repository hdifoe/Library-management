package ru.hdifoe.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hdifoe.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByFullNameStartingWith(String fullName);
    Optional<Person> findByFullName(String fullName);
} 