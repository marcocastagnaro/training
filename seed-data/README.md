# Books Seed Data - Developer Instructions

## 📚 Overview
This folder contains the seed data for the books challenge. You need to implement the functionality to load this data into your database.

## 📁 Files in this folder:
- `books-seed.json` - Contains 50 sample books with all required fields

## 🎯 Your Task
You need to create the following components to load this seed data:

### 1. Book Entity
Create a `Book` entity class with the following fields:
- `id` (String) - Primary key (auto-generated UUID as String)
- `title` (String) - Book title  
- `author` (String) - Book author
- `year` (Integer) - Publication year
- `genre` (String) - Book genre
- `isAvailable` (Boolean) - Availability status
- `language` (String) - Book language
- `pages` (Integer) - Number of pages

**Requirements:**
- Add proper JPA annotations (`@Entity`, `@Table`, `@Id`, `@GeneratedValue(strategy = GenerationType.UUID)`, `@Column`)
- Use `String` type for the ID field (UUID will be generated as String)
- Include constructors (default and all-args)
- Add getters and setters
- Add `toString()` method

### 2. Book Repository
Create a `BookRepository` interface that extends `JpaRepository<Book, String>`.

### 3. Data Loader Service
Create a service to load the seed data from the JSON file. You can:

**Option A: Use @PostConstruct**
```java
@Service
public class DataLoaderService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @PostConstruct
    public void loadBooksFromJson() {
        // TODO: Implement JSON loading logic
        // 1. Read the JSON file from classpath resources
        // 2. Parse it into List<Book>
        // 3. Save to database using bookRepository.saveAll()
    }
}
```

**Option B: Use CommandLineRunner**
```java
@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // TODO: Implement JSON loading logic
    }
}
```

### 4. JSON Parsing
You'll need to add Jackson dependency to your `build.gradle.kts`:
```kotlin
implementation("com.fasterxml.jackson.core:jackson-databind")
```

Then parse the JSON:
```java
ObjectMapper objectMapper = new ObjectMapper();
List<Book> books = objectMapper.readValue(
    new ClassPathResource("seed-data/books-seed.json").getInputStream(),
    new TypeReference<List<Book>>() {}
);
```

## 📊 Sample Book Structure
Each book in the JSON has this structure (note: no ID field, it will be auto-generated):

```json
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "publicationYear": 1925,
  "genre": "Fiction",
  "is_available": true,
  "language": "English",
  "pages": 180
}
```

## 🚀 Getting Started
1. Create the Book entity with all required fields
2. Create the BookRepository interface
3. Create a service to load the JSON data
4. Add Jackson dependency to build.gradle.kts
5. Test by running the application - it should load 50 books

## ✅ Success Criteria
- Application starts without errors
- 50 books are loaded into the database
- You can query the books through your repository
- All book fields are properly mapped

## 📝 Notes
- The JSON file is located at `src/main/resources/seed-data/books-seed.json`
- **Important**: The JSON does NOT contain ID fields - they will be auto-generated as UUID strings when created by the repository
- Books include various languages: English, Spanish, French, Italian, Russian, German, Portuguese
- Books span from 1320 to 2019 publication years
- Different genres: Fiction, Fantasy, Science Fiction, Historical Fiction, etc.
- Some books are marked as unavailable (`isAvailable: false`)

Good luck! 🎯 