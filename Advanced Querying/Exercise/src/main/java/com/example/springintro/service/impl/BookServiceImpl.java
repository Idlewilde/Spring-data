package com.example.springintro.service.impl;

import com.example.springintro.model.entity.*;
import com.example.springintro.repository.BookRepository;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    Book book = createBookFromInfo(bookInfo);

                    bookRepository.save(book);
                });
    }

    @Override
    public List<String> findAllByAgeRestriction(String ageRestriction) {
        AgeRestriction restr = AgeRestriction.valueOf(ageRestriction.toUpperCase());
        return bookRepository.findByAgeRestriction(restr);
    }

    @Override
    public List<Book> findGolden () {
        return bookRepository.findAllByCopiesGreaterThan(5000) ;
    }

    @Override
    public List<Book> findLowerThan5HigherThan40() {
        return bookRepository.findAllByPriceLowerThanOrPriceGreaterThan();
    }

    @Override
    public List<Book> findBookByReleaseDateDifferentThan(int year) {
        return bookRepository.findAllByReleaseDateNot(year);
    }

    @Override
    public List<Book> findBookByReleaseDateBefore(String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("dd-MM-yyyy");
        String dateInString = date ;
        LocalDate dateToUse = LocalDate.parse(dateInString,formatter);
        return bookRepository.findAllByReleaseDateBefore(dateToUse);
    }

    @Override
    public List<Book> findByTitleContaining(String titlePart){
        return bookRepository.findByTitleContainingIgnoreCase(titlePart);
    }

    @Override
    public List <Book> findAllByAuthorFirstNameStartingWith(String element){
        return bookRepository.findAllByAuthorFirstNameStartingWith(element);
    }

    @Override
    public int findAllByTitleLongerThan(int givenLength){
        return bookRepository.findAllByTitleLongerThan(givenLength);
    }

    @Override
    public ReducedBook findReducedBook(String title){
      return bookRepository.findReducedBook(title);
    }

    private Book createBookFromInfo(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate
                .parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction
                .values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService
                .getRandomCategories();

        return new Book(editionType, releaseDate, copies, price, ageRestriction, title, author, categories);

    }


}
