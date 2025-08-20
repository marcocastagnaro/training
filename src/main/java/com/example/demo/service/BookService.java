package com.example.demo.service;

import com.example.demo.dto.BookDto;

import java.util.List;

public interface BookService {

    public void createBook(BookDto createBookRequest);
    public List<BookDto> getAllBooks();
    public BookDto getBookById(Long id);
    public BookDto updateBookAvailability(Long id);
    public void deleteBook(Long id);

}
