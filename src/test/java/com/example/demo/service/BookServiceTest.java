package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    BookDto dummyBook = new BookDto(
            "Dummy book",
            "Dummy author",
            1943L,
            "Dummy genre",
            true,
            "Dummy language",
            96
    );

    @Test
    public void testCreateBook() {
        assert bookRepository.findAll().isEmpty();
        bookService.createBook(dummyBook);
        assert bookRepository.findAll().size() == 1;
    }

}
