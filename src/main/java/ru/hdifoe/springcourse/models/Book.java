package ru.hdifoe.springcourse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", sequenceName = "book_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Название не должно быть пустым")
    @Length(max = 100, message = "Длина названия должна быть не более 100 символов")
    @Column(name = "title")
    private String title;

    @Length(max = 100, message = "Длина имени автора должна быть не более 100 символов")
    @Column(name = "author")
    private String author;

    @PositiveOrZero(message = "Год издания не может быть отрицательным")
    @Column(name = "publication_year")
    private Long publicationYear;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;
}
