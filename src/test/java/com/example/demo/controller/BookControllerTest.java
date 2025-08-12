package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String baseUrl = "/api/book";

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

}
