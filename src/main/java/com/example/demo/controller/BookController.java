package com.example.demo.controller;


import com.example.demo.dto.BookDto;
import com.example.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
    origins = "http://localhost:5173"
)

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<String> create_book(@RequestBody BookDto createBookRequest) {
        this.bookService.createBook(createBookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado el libro correctamente");
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
      return bookService.getAllBooks();
    }

  @GetMapping("/{id}")
  public BookDto getBookById(@PathVariable Long id) {
    return bookService.getBookById(id);
  }

  @PutMapping("/{id}/available")
  public ResponseEntity<BookDto> updateBookAvailability(@PathVariable Long id) {
    return ResponseEntity.ok(bookService.updateBookAvailability(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();
  }

}
