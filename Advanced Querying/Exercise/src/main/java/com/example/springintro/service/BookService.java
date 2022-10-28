package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.ReducedBook;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List <String> findAllByAgeRestriction(String ageRestriction);

    List <Book> findGolden();

    List <Book> findLowerThan5HigherThan40();

    List <Book> findBookByReleaseDateDifferentThan (int year);

    List <Book> findBookByReleaseDateBefore (String date) throws ParseException;

    List<Book> findByTitleContaining(String titlePart);

    List <Book> findAllByAuthorFirstNameStartingWith(String element);

    int findAllByTitleLongerThan(int length);

    ReducedBook findReducedBook(String title);
}
