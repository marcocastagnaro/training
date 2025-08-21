package com.example.demo.controller;


import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<String> create_book(@Valid @RequestBody BookDto createBookRequest) {
        this.bookService.createBook(createBookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado el libro correctamente");
    }

    @PutMapping("/{id}/available")
    public ResponseEntity<String> toggleAvailability(@PathVariable String id) {
        bookService.toggleAvailability(id);
        return ResponseEntity.status(HttpStatus.OK).body("Se actualizó la disponibilidad del libro correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        bookService.delete(id);
        return ResponseEntity.ok("Eliminado");
    }
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id){
        Book book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

}
