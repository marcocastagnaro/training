package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateBookException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    BookDto dummyBook = BookDto.builder()
            .title("Dummy book")
            .author("Dummy author")
            .publicationYear(1943L)
            .genre("Dummy genre")
            .isAvailable(true)
            .language("Dummy language")
            .pages(96)
            .build();

    @BeforeEach
    void cleanDb() {
        bookRepository.deleteAll();
    }

    @Test
    public void testCreateBook() {
        assert bookRepository.findAll().isEmpty();
        bookService.createBook(dummyBook);
        assert bookRepository.findAll().size() == 1;
    }

    @Test
    void createApliesSanitizeOnStrings() {
        BookDto noisy = dummyBook.toBuilder()
                .title("  Clean   Code ")
                .author("  Robert   C.  Martin ")
                .build();
        BookDto saved = bookService.createBook(noisy);
        assertNotNull(saved.getId());
        assertEquals("Clean Code", saved.getTitle());
        assertEquals("Robert C. Martin", saved.getAuthor());
    }

    @Test
    void RejectFutureYearWithBusinessException() {
        BookDto future = dummyBook.toBuilder()
                .publicationYear((long) (Year.now().getValue() + 1))
                .build();
        assertThrows(BusinessException.class, () -> bookService.createBook(future));
    }

    @Test
    void rejectDuplicateByTitleAuthorYear() {
        var a = dummyBook.toBuilder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .publicationYear(2008L)
                .build();
        bookService.createBook(a);

        // Igual pero con mayúsculas/minúsculas distintas → puse como regla ignore case
        var dup = a.toBuilder()
                .title("clean code")
                .author("robert c. martin")
                .build();

        assertThrows(DuplicateBookException.class, () -> bookService.createBook(dup));
    }

    @Test
    void toggleAvailabilityAndDeleteFlow() {
        var created = bookService.createBook(dummyBook);
        assertTrue(created.isAvailable());

        var toggled = bookService.toggleAvailability(created.getId());
        assertFalse(toggled.isAvailable());    // pasó de true a false

        bookService.deleteBook(created.getId());
        assertThrows(NotFoundException.class, () -> bookService.findBookById(created.getId()));
    }

    @Test
    void findNonExistingThrowsNotFound() {
        assertThrows(NotFoundException.class, () -> bookService.findBookById(999_999L));
    }


}
