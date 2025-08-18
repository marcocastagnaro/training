package com.example.demo.rules;

import com.example.demo.dto.BookDto;
import com.example.demo.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class PublicationYearRule implements BusinessRule<BookDto> {
    @Override
    public void validate(BookDto bookDto) {
        int current = Year.now().getValue();
        long y = bookDto.getPublicationYear();
        if (y > current) {
            throw new BusinessException("El año de publicación no debe superar " + current);
        }
    }
}
