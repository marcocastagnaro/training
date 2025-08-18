package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.rules.BusinessRule;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final List<BusinessRule<BookDto>> bookRules;


    @Override
    public BookDto createBook(BookDto req) {
        BookDto dto = sanitize(req);
        bookRules.forEach(r -> r.validate(dto));
        Book saved = bookRepository.save(Book.from(dto));
        return BookDto.from(saved);
    }


    @Override
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream().map(BookDto::from).toList();
    }

    @Override
    public BookDto findBookById(Long id) {
        Book b = bookRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No existe el libro con id=" + id));
        return BookDto.from(b);
    }

    @Override
    public BookDto toggleAvailability(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No existe el libro con id=" + id));
        book.setAvailable(!book.isAvailable());
        return BookDto.from(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("No existe el libro con id=" + id);
        }
        bookRepository.deleteById(id);
    }


    // Helpers
    private BookDto sanitize(BookDto d) {
        return d.toBuilder()
                .title(clean(d.getTitle()))
                .author(clean(d.getAuthor()))
                .genre(clean(d.getGenre()))
                .language(clean(d.getLanguage()))
                .build();
    }

    private String clean(String s) {
        if (s == null) return null;
        return s.trim().replaceAll("\\s+", " "); // Elimina multiples espacios y espacios al principio y fin.
    }


}
