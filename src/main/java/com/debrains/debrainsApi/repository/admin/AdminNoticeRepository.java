package com.debrains.debrainsApi.repository.admin;

import com.debrains.debrainsApi.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.http.HttpHeaders;
import java.util.Optional;

@Repository
public interface AdminNoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAll(Pageable pageable);

    Optional<Notice> findById(Long id);
}
