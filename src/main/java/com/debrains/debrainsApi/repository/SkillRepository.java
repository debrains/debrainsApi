package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("SELECT DISTINCT s.category FROM Skill s ")
    List<String> getCategories();

    boolean existsByName(String name);
}
