package com.example.advquerying.services;

import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import com.example.advquerying.repositories.ShampooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class ShampooServiceImpl implements ShampooService{

    private final ShampooRepository shampooRepository;

    @Autowired
    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }

    @Override
    public List<Shampoo> selectBySize(Size size) {
        return shampooRepository.findAllBySizeOrderById(size);
    }

    @Override
    public List<Shampoo> selectBySizeOrLabelId(Size size, Long labelId) {
        return shampooRepository.findAllBySizeOrLabelIdOrderByPrice(size,labelId);
    }

    @Override
    public List<Shampoo> selectMoreExpensiveThan(BigDecimal price) {
        return shampooRepository.findAllByPriceGreaterThan(price);
    }

    @Override
    public int countPriceLowerThan(BigDecimal price) {
        return shampooRepository.countByPrice(price);
    }

    @Override
    public List<Shampoo> selectByIngredientsCount(int count) {
        return shampooRepository.countByIngredients(count);
    }

    @Override
    public List<Shampoo> selectByIngredientNames(Set<String> ingredientNames) {
        return shampooRepository.findByIngredientNames(ingredientNames);
    }


}
