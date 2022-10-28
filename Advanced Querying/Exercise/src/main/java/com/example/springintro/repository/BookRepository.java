package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.ReducedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    @Query("SELECT b.title FROM Book b WHERE b.ageRestriction = :ageRestriction")
    List<String> findByAgeRestriction(
            @Param("ageRestriction") AgeRestriction restriction);

    List <Book> findAllByCopiesGreaterThan(int copies);

    @Query("SELECT b FROM Book b WHERE b.price < 5 OR b.price > 40")
    List <Book> findAllByPriceLowerThanOrPriceGreaterThan();

    @Query("SELECT b FROM Book b WHERE YEAR(b.releaseDate) NOT IN (:year)")
    List <Book> findAllByReleaseDateNot(int year);
    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findByTitleContainingIgnoreCase(String titlePart);

    List <Book> findAllByAuthorFirstNameStartingWith(String element);

    @Query("SELECT COUNT(b.id) FROM Book b WHERE LENGTH(b.title)>:givenLength")
    int findAllByTitleLongerThan(int givenLength);

    @Query("SELECT b.title AS title," +
            " b.editionType AS editionType," +
            " b.ageRestriction AS ageRestriction," +
            " b.price AS price" +
            " FROM Book b" +
            " WHERE b.title = :title")
    ReducedBook findReducedBook(String title);
}
