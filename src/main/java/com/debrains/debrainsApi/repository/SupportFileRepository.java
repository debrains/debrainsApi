package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.common.SupportType;
import com.debrains.debrainsApi.entity.SupportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportFileRepository extends JpaRepository<SupportFile, Long> {
    @Query("select s from SupportFile s where s.supportId = :id and s.supportType = :type")
    List<SupportFile> findSupportById(@Param("id") Long id, @Param("type") SupportType type);
}
