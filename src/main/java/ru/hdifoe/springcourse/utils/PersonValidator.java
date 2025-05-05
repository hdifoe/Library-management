package ru.hdifoe.springcourse.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.hdifoe.springcourse.models.Person;
import ru.hdifoe.springcourse.repositories.PersonRepository;

import java.util.Objects;

@Component
public class PersonValidator implements Validator {
    private final PersonRepository personRepository;

    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var person = (Person) target;
        var found = personRepository.findByFullName(person.getFullName());

        if (found.isPresent() && (person.getId() == null || !Objects.equals(found.get().getId(), person.getId()))) {
            errors.rejectValue("fullName", "", "Человек с таким именем уже есть.");
        }
    }
}
