package com.example.demo.dto;

import com.example.demo.model.Book;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)

public class BookDto {
  private Long id;

  @NotBlank(message = "El título es obligatorio")
  private String title;

  @NotBlank(message = "El autor es obligatorio")
  private String author;

  @NotNull(message = "El año de publicación es obligatorio")
  @Min(value = 1, message = "El año debe ser > 1")
  private Long publicationYear;

  @NotBlank(message = "El género es obligatorio")
  private String genre;

  @Builder.Default
  private boolean isAvailable = true;

  @NotBlank(message = "El idioma es obligatorio")
  private String language;

  @Min(value = 1, message = "Las páginas deben ser >= 1")
  private int pages;


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
