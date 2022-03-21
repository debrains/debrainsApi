package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.TilCrtFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TilCrtFileRepository extends JpaRepository<TilCrtFile, Long> {
    List<TilCrtFile> findByTilCrtId(Long tilCrtId);

    @Query("SELECT f.path FROM TilCrtFile f WHERE f.tilCrt.id=:id")
    List<String> findTilCrtFile(@Param("id") Long tilCrtId);
}
