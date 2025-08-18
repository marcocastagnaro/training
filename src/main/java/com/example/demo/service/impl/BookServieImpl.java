package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServieImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServieImpl(BookRepository bookRepository1) {
        this.bookRepository = bookRepository1;
    }

    @Override
    public void createBook(BookDto createBookRequest) {
        Book book = Book.from(createBookRequest);
        bookRepository.save(book);
    }

    @Override
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream().map(BookDto::from).toList();
    }

    @Override
    public BookDto findBookById(Long id) {
        Book b = bookRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No existe el libro con id=" + id));
        return BookDto.from(b);
    }

    @Override
    public BookDto toggleAvailability(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No existe el libro con id=" + id));
        book.setAvailable(!book.isAvailable());
        return BookDto.from(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("No existe el libro con id=" + id);
        }
        bookRepository.deleteById(id);
    }



}
