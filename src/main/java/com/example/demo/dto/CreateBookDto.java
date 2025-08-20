package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateBookDto {
  String title;
  String author;
  Integer publicationYear;
  String genre;
  Boolean isAvailable;
  String language;
  Integer pages;
}
