package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;

import java.util.List;

public interface BookService {

    public void createBook(BookDto createBookRequest);

    void toggleAvailability(String id);

    void delete(String id);

    List<Book> findAll();

    Book findById(String id);
}
