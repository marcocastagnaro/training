package com.example.demo.controller;


import com.example.demo.dto.BookDto;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> create_book(@Valid @RequestBody BookDto createBookRequest) {
        BookDto created = bookService.createBook(createBookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } // Lo scambie porque me parece mas informativo y facil de usar en el front, en vez de mandar solo string.

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PutMapping("/{id}/available")
    public BookDto toggleAvailable(@PathVariable Long id) {
        return bookService.toggleAvailability(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Por defecto pq el body va vacio.
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

}
