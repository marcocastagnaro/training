package com.example.demo;

import com.example.demo.dto.CreateBookDto;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner { // Spring lo ejecuta cuando levanta la app

  private final BookRepository bookRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void run(String... args) throws Exception {
    if (bookRepository.count() > 0) return;

    var resource = new ClassPathResource("seed-data/books-seed.json");
    List<CreateBookDto> seed = objectMapper.readValue(
        resource.getInputStream(),
        new TypeReference<List<CreateBookDto>>() {}
    );

    seed.forEach(book -> {
      Book book1 = Book.from(book);
      var savedBook = bookRepository.save(book1);
      System.out.println("Book created: " + savedBook.getTitle() + " by " + savedBook.getAuthor());
        });
  }

}