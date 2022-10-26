package com.example.advquerying.repositories;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {
    List<Shampoo> findByBrand(String brand);

    List<Shampoo> findAllByBrandAndSize(String brand, Size size);

    List <Shampoo> findAllBySizeOrderById(Size size);

    List <Shampoo> findAllBySizeOrLabelIdOrderByPrice(Size size, Long id);

    List <Shampoo> findAllByPriceGreaterThan (BigDecimal price);



    @Query("SELECT s FROM Shampoo s JOIN s.ingredients AS i WHERE i.name in :ingredientNames")
    List <Shampoo> findByIngredientNames (Set<String> ingredientNames);

    @Query("SELECT COUNT(id) FROM Shampoo s WHERE s.price<:desiredPrice")
    int countByPrice (BigDecimal desiredPrice);

    @Query("SELECT s FROM Shampoo s WHERE s.ingredients.size < :count")
    List <Shampoo> countByIngredients (int count);
}
