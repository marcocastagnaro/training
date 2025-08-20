package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDto {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 250, message = "El título no puede tener más de 250 caracteres")
    private  String title;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 200, message = "El autor no puede tener más de 200 caracteres")
    private String author;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1, message = "El año de publicación debe ser mayor que 0")
    private Long publicationYear;

    @NotBlank(message = "El género es obligatorio")
    private String genre;

    @JsonProperty("available")
    private boolean isAvailable;

    @NotBlank(message = "El idioma es obligatorio")
    private String language;

    @NotNull(message = "La cantidad de páginas es obligatoria")
    @Min(value = 1, message = "La cantidad de páginas debe ser al menos 1")
    private Integer pages;
}
