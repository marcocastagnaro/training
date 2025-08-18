package com.example.demo.dto;

import com.example.demo.model.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 0, message = "El año no puede ser negativo")
    private Long publicationYear;

    @NotBlank(message = "El género es obligatorio")
    private String genre;

    @Builder.Default
    private boolean isAvailable = true;

    @NotBlank(message = "El idioma es obligatorio")
    private String language;

    @Min(value = 1, message = "Las páginas deben ser ≥ 1")
    private int pages; // No le pongo not null porque ya es un int.

    public static BookDto from(Book b) {
        return BookDto.builder()
                .id(b.getId())
                .title(b.getTitle())
                .author(b.getAuthor())
                .publicationYear(b.getPublicationYear())
                .genre(b.getGenre())
                .isAvailable(b.isAvailable())
                .language(b.getLanguage())
                .pages(b.getPages())
                .build();
    }
}
