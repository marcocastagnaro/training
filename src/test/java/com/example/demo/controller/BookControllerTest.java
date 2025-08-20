package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BookRepository repo;

    private final String baseUrl = "/api/books";

    @AfterEach
    void clean() {
        repo.deleteAll();
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
    void testFutureYearReturns400() {
        long nextYear = java.time.Year.now().getValue() + 1L;
        BookDto dto = BookDto.builder()
                .title("Dummy book")
                .author("Dummy author")
                .publicationYear(nextYear)
                .genre("Dummy genre")
                .isAvailable(true)
                .language("Dummy language")
                .pages(96)
                .build();
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.POST, new HttpEntity<>(dto), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private Book save(String title, boolean available) {
        BookDto dto = BookDto.builder()
                .title(title)
                .author("Dummy author")
                .publicationYear(2000L)
                .genre("Dummy genre")
                .isAvailable(available)
                .language("Dummy language")
                .pages(96)
                .build();
        return repo.save(Book.from(dto));
    }

    @Test
    void testGet() {
        save("title1", true);
        save("title2", false);
        ResponseEntity<Book[]> res = testRestTemplate.getForEntity(baseUrl, Book[].class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().length >= 2);
    }

    @Test
    void testGetBooksById() {
        Book saved = save("Title", true);
        ResponseEntity<Book> response = testRestTemplate.getForEntity(baseUrl + "/" + saved.getId(), Book.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(saved.getId(), response.getBody().getId());
    }

    @Test
    void testToggleAvailability() {
        Book saved = save("ToggleMe", true);
        ResponseEntity<String> response = testRestTemplate.exchange(
                baseUrl + "/" + saved.getId() + "/available",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Checkeo que cambio en db
        Boolean after = repo.findById(saved.getId()).orElseThrow().isAvailable();
        assertFalse(after);
    }

    @Test
    void testDeleteBook() {
        Book saved = save("delete", true);
        ResponseEntity<String> response = testRestTemplate.exchange(
                baseUrl + "/" + saved.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(repo.findById(saved.getId()).isEmpty());
    }
    @Test
    void testPostingBadFormatJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String brokenJson = "{\"title\":\"d\",\"author\":\"Robert\"";
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.POST, new HttpEntity<>(brokenJson, headers), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testInvalidTypesInJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String wrongTypesJson = """
        {
          "title": 123,
          "author": true,
          "publicationYear": "veinte veinte",
          "genre": null,
          "available": "si",
          "language": [],
          "pages": "noventa y seis"
        }
        """;
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.POST, new HttpEntity<>(wrongTypesJson, headers), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}