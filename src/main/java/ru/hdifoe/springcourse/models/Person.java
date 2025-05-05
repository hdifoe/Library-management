package ru.hdifoe.springcourse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    @SequenceGenerator(name = "person_seq", sequenceName = "person_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "ФИО не может быть пустым")
    @Length(max = 100, message = "Длина ФИО должна быть не более 100 символов")
    @Column(name = "full_name")
    private String fullName;

    @NotNull(message = "Год рождения не может быть пустым")
    @PositiveOrZero(message = "Год рождения не может быть отрицательным")
    @Column(name = "birth_year")
    private Long birthYear;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
}
