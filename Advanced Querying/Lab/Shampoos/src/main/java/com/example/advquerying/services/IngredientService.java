package com.example.advquerying.services;

import com.example.advquerying.entities.Ingredient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface IngredientService {

    List<Ingredient> selectNameStartsWith(String start);

    List<Ingredient> selectInNames(Set<String> names);

    int deleteByName(String name);

    void increasePriceByPercentage(double percent);

    void increasePriceByPercentageAndName(double multiplier, Set <String> ingredientNames);
}
