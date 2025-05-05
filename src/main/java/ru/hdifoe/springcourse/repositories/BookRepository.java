package ru.hdifoe.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hdifoe.springcourse.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByPersonId(Long personId);
    List<Book> findByTitleStartingWith(String title);
} 