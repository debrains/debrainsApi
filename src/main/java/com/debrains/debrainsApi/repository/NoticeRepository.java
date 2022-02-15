package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
