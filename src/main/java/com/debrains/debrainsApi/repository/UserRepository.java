package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"role"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select u from User u where u.email = :email")
    Optional<User> findByUsername(String email);

}
