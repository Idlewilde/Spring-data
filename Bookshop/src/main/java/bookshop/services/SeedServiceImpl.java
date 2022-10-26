package bookshop.services;

import bookshop.models.AgeRestriction;
import bookshop.models.Author;
import bookshop.models.Book;
import bookshop.models.Category;
import bookshop.models.EditionType;
import bookshop.repositories.AuthorRepository;
import bookshop.repositories.BookRepository;
import bookshop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {

    private static final String RESOURCE_PATH ="src/main/resources/files";
    private static final String AUTHORS_FILE_PATH =RESOURCE_PATH+"/authors.txt";
    private static final String CATEGORIES_FILE_PATH =RESOURCE_PATH+"/categories.txt";
    private static final String BOOKS_FILE_PATH =RESOURCE_PATH+"/books.txt";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired BookService bookService;

    @Override
    public void seedAuthors() throws IOException {
        Files.readAllLines(Path.of(AUTHORS_FILE_PATH)).stream()
                .filter(s -> !s.isBlank())
                .map(s -> s.split(" "))
                .map(names -> new Author(names[0], names[1]))
                .forEach(authorRepository::save);
    }

    @Override
    public void seedCategories() throws IOException {
        Files.readAllLines(Path.of(CATEGORIES_FILE_PATH)).stream()
                .filter(s -> !s.isBlank())
                .map(Category::new)
                .forEach(categoryRepository::save);

    }

    @Override
    public void seedBooks() throws IOException {
        Files.readAllLines(Path.of(BOOKS_FILE_PATH)).stream()
                .filter(s -> !s.isBlank())
                .map(this::createBook)
                .forEach(bookRepository::save);
    }

    private Book createBook(String book) {
        String[] bookParts = book.split(" ");
        EditionType editionType = EditionType.values()[Integer.parseInt(bookParts[0])];
        LocalDate publishDate =
                LocalDate.parse(bookParts[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        int copies = Integer.parseInt(bookParts[2]);
        double price = Double.parseDouble(bookParts[3]);

        int ageRestrictionIndex = Integer.parseInt(bookParts[4]);
        AgeRestriction ageRestriction = AgeRestriction.values()[ageRestrictionIndex];

        String title = Arrays.stream(bookParts)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService.getRandomCategories();

        return new Book(title, editionType, price, copies, publishDate,
                ageRestriction, author, categories);
    }
}
