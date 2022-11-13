package com.example.springintro.repository;

import com.example.springintro.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    Game findById(Long id);

    List<Game> findAll();

    Game findByTitle(String title);


}
