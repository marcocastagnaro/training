package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.springframework.stereotype.Service;

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
}
