package com.example.springintro.repository;

import com.example.springintro.model.entity.Game;
import com.example.springintro.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByIsLogged(boolean isLogged);


}
