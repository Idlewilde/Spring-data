package com.example.football.repository;

import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//ToDo:
public interface StatRepository  extends JpaRepository<Stat, Long> {
    Optional<Stat> findByEnduranceAndPassingAndShooting(float endurance, float passing, float shooting);
}
