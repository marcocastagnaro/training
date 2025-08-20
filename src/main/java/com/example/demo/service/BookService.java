package com.example.demo.service;

import com.example.demo.dto.BookDto;

import java.util.List;

public interface BookService {

    List<BookDto> findAllBooks();

    BookDto findBookById(Long id);

    BookDto createBook(BookDto createBookRequest);

    BookDto toggleAvailability(Long id);

    void deleteBook(Long id);

}
