package com.example.springintro.service;

import com.example.springintro.model.entity.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    List <Author> findAllByFirstNameEndingWith(String element);

    int findBookCount(String name);

    Author getRandomAuthor();
}
