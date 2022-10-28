package com.example.springintro;

import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.ReducedBook;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        //task 0
        //seedData();

        //task 1
        // bookService.findAllByAgeRestriction("teEN").forEach(System.out::println);

        //task 2
        // bookService.findGolden().forEach(e-> System.out.println(e.getTitle()));

        //task 3
        //bookService.findLowerThan5HigherThan40().forEach(e-> System.out.println(e.getTitle()+" - "+e.getPrice()));

        //task 4
        //bookService.findBookByReleaseDateDifferentThan(2000).forEach(e-> System.out.println(e.getTitle()));

        //task 5
        //bookService.findBookByReleaseDateBefore("12-04-1992").forEach(e-> System.out.println(e.getTitle()+" "+e.getPrice()));

        //task 6
        //authorService.findAllByFirstNameEndingWith("dy").forEach(e-> System.out.println(e.getFirstName()+" "+e.getLastName()));

        //task 7
        //bookService.findByTitleContaining("sK").forEach(e-> System.out.println(e.getTitle()));

        //task8
        //bookService.findAllByAuthorFirstNameStartingWith("Jul").forEach(e-> System.out.println(e.getAuthor().getFirstName()+" "+e.getAuthor().getLastName()+" "+e.getTitle()));

        //task9
        //System.out.println("There are "+bookService.findAllByTitleLongerThan(40)
        //                +" books with title longer than 40 characters.");

        //task10
        //System.out.println(authorService.findBookCount("Randy Graham"));

        //task11
        // ReducedBook book = bookService.findReducedBook("Things Fall Apart");
        //System.out.printf("%s %s %s %.2f%n",book.getTitle(),book.getAgeRestriction().name(), book.getEditionType().name(), book.getPrice());


    }


    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
