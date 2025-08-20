package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @AfterEach
    void clean() {
        bookRepository.deleteAll();
    }

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
    private BookDto.BookDtoBuilder dto() {
        return BookDto.builder()
                .title("Dummy book")
                .author("Dummy author")
                .publicationYear(1943L)
                .genre("Dummy genre")
                .isAvailable(true)
                .language("Dummy language")
                .pages(96);
    }

    @Test
    void testSaves() {
        assertEquals(0, bookRepository.count());
        bookService.createBook(dto().build());
        assertEquals(1, bookRepository.count());
        Book saved = bookRepository.findAll().get(0);
        assertEquals("Dummy book", saved.getTitle());
        assertTrue(saved.isAvailable());
    }

    @Test
    void testFutureYear_throws400() {
        long nextYear = Year.now().getValue() + 1L;
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookService.createBook(dto().publicationYear(nextYear).build()));
        assertEquals(400, ex.getStatusCode().value());
        assertEquals(0, bookRepository.count());
    }

    @Test
    void testFindAll() {
        bookService.createBook(dto().title("1").publicationYear(1999L).build());
        bookService.createBook(dto().title("2").publicationYear(2001L).isAvailable(false).build());
        List<Book> all = bookService.findAll();
        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void testFindById() {
        bookService.createBook(dto().title("Unique").build());
        String id = bookRepository.findAll().get(0).getId();
        Book b = bookService.findById(id);
        assertNotNull(b);
        assertEquals("Unique", b.getTitle());
    }

    @Test
    void testToggleAvailability() {
        bookService.createBook(dto().isAvailable(true).build());
        Book before = bookRepository.findAll().get(0);
        assertTrue(before.isAvailable());
        bookService.toggleAvailability(before.getId());
        Book after = bookRepository.findById(before.getId()).orElseThrow();
        assertFalse(after.isAvailable());
    }

    @Test
    void testDelete() {
        bookService.createBook(dto().title("ToDelete").build());
        String id = bookRepository.findAll().get(0).getId();
        bookService.delete(id);
        assertTrue(bookRepository.findById(id).isEmpty());
    }
}
