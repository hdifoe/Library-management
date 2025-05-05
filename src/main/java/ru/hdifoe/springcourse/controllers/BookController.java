package ru.hdifoe.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.hdifoe.springcourse.models.Book;
import ru.hdifoe.springcourse.models.Person;
import ru.hdifoe.springcourse.repositories.BookRepository;
import ru.hdifoe.springcourse.repositories.PersonRepository;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    public BookController(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    @GetMapping
    public String index(Model model, @RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("books", bookRepository.findByTitleStartingWith(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("books", bookRepository.findAll());
        }
        return "books/index";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("book", new Book());
        return "books/create";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult errors) {
        if (errors.hasErrors()) {
            return "books/create";
        }

        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String readPage(@PathVariable Long id, Model model) {
        var books = bookRepository.findAll().stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
                
        if (books.isEmpty()) {
            return "redirect:/books";
        }
        
        Book book = books.get();
        model.addAttribute("book", book);
        
        if (book.getPerson() != null) {
            model.addAttribute("person", book.getPerson());
        } else {
            model.addAttribute("people", personRepository.findAll());
        }
        return "books/read";
    }

    @GetMapping("/{id}/update")
    public String updatePage(@PathVariable Long id, Model model) {
        var book = bookRepository.findAll().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
                
        if (book == null) {
            return "redirect:/books";
        }
        
        if (book.getPerson() != null) {
            model.addAttribute("error", "Нельзя редактировать книгу, которая находится на руках у читателя");
            return "books/read";
        }
        
        model.addAttribute("book", book);
        return "books/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult errors) {
        if (errors.hasErrors()) {
            return "books/update";
        }

        bookRepository.save(book);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        bookRepository.deleteAllById(Collections.singleton(id));
        return "redirect:/books";
    }

    @PatchMapping("/{id}/change_owner")
    public String change_owner(@PathVariable Long id, @RequestParam(value = "personId", required = false) Long personId) {
        var bookOptional = bookRepository.findAll().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
                
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (personId != null) {
                Optional<Person> personOptional = personRepository.findById(personId);
                personOptional.ifPresent(book::setPerson);
            } else {
                book.setPerson(null);
            }
            bookRepository.save(book);
        }
        return "redirect:/books/{id}";
    }
}
