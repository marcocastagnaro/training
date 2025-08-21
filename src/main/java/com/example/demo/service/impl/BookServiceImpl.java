package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository1) {
        this.bookRepository = bookRepository1;
    }

    @Override
    public void createBook(BookDto createBookRequest) {
        if (createBookRequest.getPublicationYear() != null && createBookRequest.getPublicationYear()
        > Year.now().getValue()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El año de publicación no puede ser futuro"); //400
        }
        Book book = Book.from(createBookRequest);
        bookRepository.save(book);
    }

    @Override
    public void toggleAvailability(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un libro con id " + id));
        book.setAvailable(!book.isAvailable());
        bookRepository.save(book);
    }

    @Override
    public void delete(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un libro con id " + id));
        bookRepository.delete(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe un libro con id " + id));
    }
}
