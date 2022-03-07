package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Qna;
import com.debrains.debrainsApi.entity.TilCrtFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TilCrtFileRepository extends JpaRepository<TilCrtFile, Long> {
    List<TilCrtFile> findByTilCrtId(Long tilCrtId);
}
