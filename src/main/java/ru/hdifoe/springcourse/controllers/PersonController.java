package ru.hdifoe.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.hdifoe.springcourse.models.Person;
import ru.hdifoe.springcourse.repositories.PersonRepository;
import ru.hdifoe.springcourse.utils.PersonValidator;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonRepository personRepository;
    private final PersonValidator personValidator;

    public PersonController(PersonRepository personRepository, PersonValidator personValidator) {
        this.personRepository = personRepository;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String index(Model model, @RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("people", personRepository.findByFullNameStartingWith(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("people", personRepository.findAll());
        }
        return "people/index";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("person", new Person());
        return "people/create";
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult errors) {
        personValidator.validate(person, errors);

        if (errors.hasErrors()) {
            return "people/create";
        }

        person.setBooks(new ArrayList<>());
        personRepository.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String readPage(@PathVariable Long id, Model model) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            return "redirect:/people";
        }
        
        Person person = personOptional.get();
        model.addAttribute("person", person);
        model.addAttribute("books", person.getBooks());
        return "people/read";
    }

    @GetMapping("/{id}/update")
    public String updatePage(@PathVariable Long id, Model model) {
        model.addAttribute("person", personRepository.findById(id).orElse(null));
        return "people/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult errors) {
        personValidator.validate(person, errors);

        if (errors.hasErrors()) {
            return "people/update";
        }

        personRepository.save(person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        personRepository.deleteById(id);
        return "redirect:/people";
    }
}
