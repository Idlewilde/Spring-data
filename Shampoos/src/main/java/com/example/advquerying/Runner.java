package com.example.advquerying;

import com.example.advquerying.repositories.IngredientRepository;
import com.example.advquerying.repositories.ShampooRepository;
import com.example.advquerying.services.IngredientService;
import com.example.advquerying.services.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Set;

@Component
public class Runner implements CommandLineRunner {

    private final ShampooRepository shampooRepository;
    private final IngredientRepository ingredientRepository;

    private final ShampooService shampooService;

    private final IngredientService ingredientService;

    @Autowired
    public Runner(ShampooRepository shampooRepository, IngredientRepository ingredientRepository, ShampooService shampooService, IngredientService ingredientService) {
        this.shampooRepository = shampooRepository;
        this.ingredientRepository = ingredientRepository;
        this.shampooService = shampooService;
        this.ingredientService = ingredientService;
    }


    @Override
@Transactional
    public void run(String... args) throws Exception {

    }
}
