package com.example.demo.model;

import com.example.demo.dto.BookDto;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Long publicationYear;

    @Column(nullable = false)
    private String genre;


    @JsonProperty("isAvailable")
    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private int pages;

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
