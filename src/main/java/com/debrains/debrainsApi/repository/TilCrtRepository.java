package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.TilCrt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TilCrtRepository extends JpaRepository<TilCrt, Long> {
    @Query("select tr from TilCrt tr join tr.til t where t.id = :id")
    Page<TilCrt> findAllById(@Param("id") Long id, Pageable pageable);

    @Query("SELECT count(t.id) FROM TilCrt t " +
            "WHERE t.user.id=:id and t.til.id=:tilId " +
            "and t.regDate>=:start and t.regDate<=:end and t.denied=false")
    Long tilCrtCount(@Param("id") Long userId, Long tilId, LocalDateTime start, LocalDateTime end);

    List<TilCrt> findTilCrtByUser_Id(Long userId, Pageable pageable);
}
