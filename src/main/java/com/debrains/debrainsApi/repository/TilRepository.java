package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Til;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TilRepository extends JpaRepository<Til, Long> {
}
