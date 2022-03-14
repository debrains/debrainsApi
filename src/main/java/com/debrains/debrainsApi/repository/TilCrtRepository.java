package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.TilCrt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TilCrtRepository extends JpaRepository<TilCrt, Long> {

    @Query("SELECT count(t.id) FROM TilCrt t " +
            "WHERE t.user.id=:id and t.regDate>=:start and t.regDate<=:end and t.denied=false")
    Long tilCrtCount(@Param("id") Long userId, LocalDateTime start, LocalDateTime end);
}
