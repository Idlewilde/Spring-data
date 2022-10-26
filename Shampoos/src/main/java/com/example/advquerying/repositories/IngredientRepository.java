package com.example.advquerying.repositories;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.entities.Shampoo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface IngredientRepository extends JpaRepository <Ingredient, Long> {

    List<Ingredient> findAllByNameStartingWith(String letter);
    List <Ingredient> findByNameIn (Set<String> ingredientNames);

    int deleteByName(String name);

    @Modifying
    @Query("UPDATE Ingredient i SET i.price = i.price + i.price * :multiplier")
    void increasePriceByPercent(@Param("multiplier") BigDecimal percent);

    @Modifying
    @Query("UPDATE Ingredient i SET i.price = i.price + i.price * :multiplier WHERE i.name in :ingredientNames")
    void increasePriceByPercentAndName(BigDecimal multiplier, Set<String> ingredientNames);
}
