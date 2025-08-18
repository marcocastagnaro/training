package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private BookRepository repo;
    @Autowired
    private ObjectMapper om;

    private final String baseUrl = "/books";

    @BeforeEach
    void cleanDb() { repo.deleteAll(); }

    // helpers

    private BookDto book(String title, int year) {
        return BookDto.builder()
                .title(title)
                .author("Author")
                .publicationYear((long) year)
                .genre("Genre")
                .language("ES")
                .pages(100)
                .build();
    }

    private BookView create(BookDto req) {
        ResponseEntity<BookView> r = testRestTemplate.postForEntity(baseUrl, req, BookView.class);
        assertEquals(HttpStatus.CREATED, r.getStatusCode());
        assertNotNull(r.getBody());
        return r.getBody();
    }

    private BookView get(long id) {
        ResponseEntity<BookView> r = testRestTemplate.getForEntity(baseUrl + "/" + id, BookView.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        return r.getBody();
    }

    private List<BookView> list() {
        ResponseEntity<BookView[]> r = testRestTemplate.getForEntity(baseUrl, BookView[].class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        return List.of(r.getBody());
    }

    private BookView toggle(long id) {
        ResponseEntity<BookView> r = testRestTemplate.exchange(baseUrl + "/" + id + "/available",
                HttpMethod.PUT, HttpEntity.EMPTY, BookView.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        return r.getBody();
    }

    // Para mandar json crudo
    private ResponseEntity<String> postRaw(String json) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return testRestTemplate.exchange(baseUrl, HttpMethod.POST, new HttpEntity<>(json, h), String.class);
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

    private void delete(long id) {
        ResponseEntity<Void> r = testRestTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, r.getStatusCode());
    }

    // Happy path

    @Test
    void createGetListToggleDelete() {
        var created = create(BookDto.builder()
                .title("Clean Code").author("Robert C. Martin")
                .publicationYear(2008L).genre("Programming")
                .language("ES").pages(464).build());

        assertNotNull(created.id);

        var fetched = get(created.id);
        assertEquals("Clean Code", fetched.title);

        var all = list();
        assertEquals(1, all.size());

        var toggled = toggle(created.id);
        assertFalse(toggled.available);

        var del = testRestTemplate.exchange(baseUrl + "/" + created.id, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, del.getStatusCode());

        var nf = testRestTemplate.getForEntity(baseUrl + "/" + created.id, String.class);
        assertEquals(HttpStatus.NOT_FOUND, nf.getStatusCode());
    }

    @Test
    void postInvalidDtoReturns400() {
        // falta title y pages = 0 → viola @NotBlank y @Min
        String invalid = """
      {"author":"Yo","publicationYear":2020,"genre":"G","language":"ES","pages":0}
    """;
        var resp = postRaw(invalid);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Validación fallida"));
        assertTrue(resp.getBody().contains("title"));
        assertTrue(resp.getBody().contains("pages"));
    }

    @Test
    void postMalformedJsonEeturns400() {
        // pages como string → HttpMessageNotReadableException
        String malformed = """
      {"title":"A","author":"B","publicationYear":2020,"genre":"G","language":"ES","pages":"xxx"}
    """;
        var resp = postRaw(malformed);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("JSON malformado"));
    }

    @Test
    void getNotFoundReturns404() {
        var resp = testRestTemplate.getForEntity(baseUrl + "/999999", String.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertTrue(resp.getBody().contains("No existe el libro"));
    }

    @Test
    void postDplicateReturns409() {
        var dto = book("Clean Code", 2008);
        create(dto); // primero OK
        var dup = testRestTemplate.postForEntity(baseUrl, dto, String.class);
        assertEquals(HttpStatus.CONFLICT, dup.getStatusCode());
        assertTrue(dup.getBody().contains("Ya existe un libro"));
    }








}
