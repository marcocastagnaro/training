package com.example.demo.seed;

import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class DataLoaderIT {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataLoader dataLoader;

    @Test
    void seed_is_loaded_on_startup_with_expected_count() throws Exception {
        // Leemos el mismo JSON de la seed para obtener el número esperado
        var resource = new ClassPathResource("seed-data/books-seed.json");
        List<Map<String, Object>> booksFromJson = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<Map<String, Object>>>() {}
        );

        long expected = booksFromJson.size();
        long actual = bookRepository.count();

        assertEquals(expected, actual,
                "La cantidad de libros en BD debe coincidir con la cantidad del JSON");
    }

    @Test
    void data_loader_is_idempotent_does_not_duplicate() throws Exception {
        long before = bookRepository.count();

        dataLoader.run();

        long after = bookRepository.count();
        assertEquals(before, after, "Volver a ejecutar el loader no debe duplicar registros");
    }
}
