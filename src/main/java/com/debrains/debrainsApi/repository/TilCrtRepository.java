package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.TilCrt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TilCrtRepository extends JpaRepository<TilCrt, Long> {
    @Query("select tr from TilCrt tr join tr.til t where t.id = :id")
    Page<TilCrt> findAllById(@Param("id") Long id, Pageable pageable);
}
