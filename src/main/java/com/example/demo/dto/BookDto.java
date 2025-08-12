package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDto {
    private  String title;
    private String author;
    private Long publicationYear;
    private String genre;
    private boolean isAvailable;
    private String language;
    private int pages;
}
