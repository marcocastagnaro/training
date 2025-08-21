package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

  @Autowired
  private BookRepository bookRepository; //uso repo para limpiar la base de datos

    private final String baseUrl = "/api/books";

  @BeforeEach
  void cleanDb() {
    bookRepository.deleteAll();
  }

    @Test
    void createBook() {
        BookDto createBookDto = BookDto.builder()
                .title("Dummy book")
                .author("Dummy author")
                .publicationYear(1000L)
                .genre("Dummy genre")
                .isAvailable(true)
                .language("Dummy language")
                .pages(96)
                .build();
        ResponseEntity<String> response = this.testRestTemplate.exchange(this.baseUrl, HttpMethod.POST, new HttpEntity<>(createBookDto), String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

  @Test
  void getAllBooks() {
    post(build("A", "AA", 1999L)); //creo libro A
    post(build("B", "BB", 2001L)); //creo libro B

    //llamo al endpoint
    ResponseEntity<BookDto[]> resp = testRestTemplate.getForEntity(baseUrl, BookDto[].class);

    //verifico q la rta sea OK
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    BookDto[] list = Objects.requireNonNull(resp.getBody());
    assertEquals(2, list.length); //deben haber 2 libros
    assertTrue(Arrays.stream(list).anyMatch(b -> "A".equals(b.getTitle())));
    assertTrue(Arrays.stream(list).anyMatch(b -> "B".equals(b.getTitle())));
  }


  @Test
  void getBookById() {
    // se fija que get devuelvza un libro especifico
    BookDto dto = build("Clean Code", "Robert C. Martin", 2008L);
    post(dto);//creo libro
    Long id = bookRepository.findAll().get(0).getId(); //obtengo el id del libro

    //llamo al endpoint
    ResponseEntity<BookDto> resp = testRestTemplate.getForEntity(baseUrl + "/" + id, BookDto.class);

    //verifico q devuelva ok
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    BookDto body = Objects.requireNonNull(resp.getBody());
    //verifico que el libro sea el correcto
    assertEquals("Clean Code", body.getTitle());
    assertEquals("Robert C. Martin", body.getAuthor());
    assertEquals(2008L, body.getPublicationYear());
    assertEquals(id, body.getId());
  }

  @Test
  void updateAvailability() {
    // verifica que put /books/{id}/available cambia el estado de disp
    BookDto dto = build("Dune", "Frank Herbert", 1965L);
    dto.setAvailable(true);
    post(dto);
    Long id = bookRepository.findAll().get(0).getId();

    //llamo al endpoint
    ResponseEntity<BookDto> resp = testRestTemplate.exchange(
        baseUrl + "/" + id + "/available",
        HttpMethod.PUT,
        HttpEntity.EMPTY,
        BookDto.class
    );

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    BookDto updated = Objects.requireNonNull(resp.getBody());
    assertFalse(updated.isAvailable()); //el campo available debe haberse invertido
  }

  @Test
  void deleteBook() {
    // verifica que delete /books/{id} elimina un libro
    BookDto dto = build("To Delete", "Someone", 2020L);
    post(dto);//creo libro
    Long id = bookRepository.findAll().get(0).getId();

    ResponseEntity<Void> resp = testRestTemplate.exchange(
        baseUrl + "/" + id,
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        Void.class
    );

    //verifico q rta sea no content
    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    //confrmo q el libro no existe mas
    BookDto[] after = testRestTemplate.getForObject(baseUrl, BookDto[].class);
    assertNotNull(after);
    assertTrue(Arrays.stream(after).noneMatch(b -> id.equals(b.getId())));
  }

  private BookDto build(String title, String author, Long year) {
    //construye un DTO con valores por defecto para reutilizar
    return BookDto.builder()
        .title(title)
        .author(author)
        .publicationYear(year)
        .genre("Fiction")
        .isAvailable(true)
        .language("EN")
        .pages(123)
        .build();
  }

  //helper para hacer post para crrear linro
  private void post(BookDto dto) {
    //hace un post
    ResponseEntity<String> response =
        testRestTemplate.postForEntity(baseUrl, dto, String.class);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

}
