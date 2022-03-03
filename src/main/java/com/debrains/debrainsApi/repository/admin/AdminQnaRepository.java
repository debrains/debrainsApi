package com.debrains.debrainsApi.repository.admin;


import com.debrains.debrainsApi.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminQnaRepository extends JpaRepository<Qna, Long> {
}
