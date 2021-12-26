package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select u from User u where u.fromSocial = :social and u.email = :email")
    Optional<User> findByEmail(String email, boolean social);

}
