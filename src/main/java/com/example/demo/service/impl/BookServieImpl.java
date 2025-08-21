package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookDto::from).toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return BookDto.from(book);
    }

  @Override
  public BookDto updateBookAvailability(Long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    book.setAvailable(!book.isAvailable());
    return BookDto.from(bookRepository.save(book));
  }

  @Override
  public void deleteBook(Long id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
    } else {
      throw new RuntimeException("Book not found with id: " + id);
    }
  }
}
