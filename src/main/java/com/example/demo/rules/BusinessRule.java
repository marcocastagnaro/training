package com.example.demo.rules;

import com.example.demo.dto.BookDto;

public interface BusinessRule<T> {
    void validate(T bookDto);
}
