package com.example.demo.model;

import com.example.demo.dto.BookDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "publication_year", nullable = false)
    private Long publicationYear;

    @Column(nullable = false)
    private String genre;

    @JsonAlias({"is_available", "isAvailable"})
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Integer pages;

    public static Book from(BookDto createBookRequest) {
        return Book.builder()
            .title(createBookRequest.getTitle())
            .author(createBookRequest.getAuthor())
            .publicationYear(createBookRequest.getPublicationYear())
            .genre(createBookRequest.getGenre())
            .isAvailable(createBookRequest.isAvailable())
            .language(createBookRequest.getLanguage())
            .pages(createBookRequest.getPages())
            .build();
    }

}
