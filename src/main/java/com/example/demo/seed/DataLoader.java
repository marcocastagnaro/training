package com.example.demo.seed;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner { // Spring lo ejecuta cuando levanta la app

    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() > 0) return;

        var resource = new ClassPathResource("seed-data/books-seed.json");
        List<Book> seed = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<Book>>() {}
        );

        // Filtramos duplicados (opcional)
        var toSave = seed.stream()
                .filter(s -> !bookRepository.existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndPublicationYear(
                        s.getTitle(), s.getAuthor(), s.getPublicationYear()))
                .toList();

        bookRepository.saveAll(toSave);
        System.out.println("Seed cargada: " + toSave.size() + " libros");

    }
}
