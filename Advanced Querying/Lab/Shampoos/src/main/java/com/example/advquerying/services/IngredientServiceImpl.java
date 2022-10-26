package com.example.advquerying.services;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class IngredientServiceImpl implements IngredientService{

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> selectNameStartsWith(String start) {
        return ingredientRepository.findAllByNameStartingWith(start);
    }

    @Override
    public List<Ingredient> selectInNames(Set<String> names) {
        return ingredientRepository.findByNameIn(names);
    }

    @Transactional
    @Override
    public int deleteByName(String name) {
        return ingredientRepository.deleteByName(name);
    }

    @Override
    public void increasePriceByPercentage(double percent) {
        BigDecimal actualPercent = BigDecimal.valueOf(percent);

        this.ingredientRepository.increasePriceByPercent(actualPercent);
    }

    @Override
    public void increasePriceByPercentageAndName(double multiplier, Set<String> ingredientNames) {
        this.ingredientRepository.increasePriceByPercentAndName(BigDecimal.valueOf(multiplier),ingredientNames);
    }
}
