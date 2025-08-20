package com.example.demo.rules;

import com.example.demo.dto.BookDto;
import com.example.demo.exception.DuplicateBookException;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DuplicatedBookRule implements BusinessRule<BookDto> {

    private final BookRepository repo;

    @Override
    public void validate(BookDto bookDto) {
        if (repo.existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndPublicationYear(
                bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPublicationYear())) {
            throw new DuplicateBookException("Ya existe un libro con ese título, autor y año.");
        }
    }
}
