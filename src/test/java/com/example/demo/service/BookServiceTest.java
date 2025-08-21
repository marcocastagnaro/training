package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BookServiceTest {

  @Autowired
  private BookService bookService;

  @Autowired
  private BookRepository bookRepository;

  @BeforeEach
  void cleanDb() {
    bookRepository.deleteAll();
  }

  private static BookDto sampleDto(boolean isAvailable) {
    return BookDto.builder()
        .title("Dummy book")
        .author("Dummy author")
        .publicationYear(1943L)
        .genre("Dummy genre")
        .language("Dummy language")
        .pages(96)
        .isAvailable(isAvailable)
        .build();
  }

  @Test
  void createBook() {
    bookService.createBook(sampleDto(true)); //creo libro

    assertThat(bookRepository.count()).isEqualTo(1);
    var saved = bookRepository.findAll().get(0);
    assertThat(saved.getTitle()).isEqualTo("Dummy book");
    assertThat(saved.getAuthor()).isEqualTo("Dummy author");
    assertThat(saved.getPublicationYear()).isEqualTo(1943L);
    assertThat(saved.getGenre()).isEqualTo("Dummy genre");
    assertThat(saved.getLanguage()).isEqualTo("Dummy language");
    assertThat(saved.getPages()).isEqualTo(96);
    assertThat(saved.isAvailable()).isTrue();
  }

  @Test
  void getAllBooks() {
    bookService.createBook(sampleDto(true));
    bookService.createBook(sampleDto(false));

    var result = bookService.getAllBooks();

    assertThat(result).hasSize(2);
    assertThat(result)
        .extracting(BookDto::getTitle)
        .containsExactlyInAnyOrder("Dummy book", "Dummy book");
  }

  @Test
  void getBookById_returnsDto() {
    bookService.createBook(sampleDto(true)); //creo libro
    var savedId = bookRepository.findAll().get(0).getId(); //obtengo id

    var dto = bookService.getBookById(savedId);

    assertThat(dto.getId()).isEqualTo(savedId);
    assertThat(dto.getTitle()).isEqualTo("Dummy book");
  }

  @Test
  void getBookById() {
    assertThatThrownBy(() -> bookService.getBookById(999L))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void updateBookAvailability() {
    bookService.createBook(sampleDto(true));
    var saved = bookRepository.findAll().get(0);
    var id = saved.getId();

    var updatedDto = bookService.updateBookAvailability(id);

    assertThat(updatedDto.isAvailable()).isFalse();
    var reloaded = bookRepository.findById(id).orElseThrow();
    assertThat(reloaded.isAvailable()).isFalse();
  }

  @Test
  void deleteBook() {
    bookService.createBook(sampleDto(true));
    var id = bookRepository.findAll().get(0).getId();

    bookService.deleteBook(id);

    assertThat(bookRepository.existsById(id)).isFalse();
    assertThat(bookRepository.findAll()).isEmpty();
  }
}