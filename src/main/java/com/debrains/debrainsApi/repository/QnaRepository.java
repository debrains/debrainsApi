package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findByUserId(Long userId);
}
