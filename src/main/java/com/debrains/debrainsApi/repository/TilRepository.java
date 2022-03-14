package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.security.CurrentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TilRepository extends JpaRepository<Til, Long> {

    @Query("SELECT COUNT(t.id) FROM Til t WHERE t.user.id=:id")
    Long totalTil(@Param("id") Long userId);

    @Query("SELECT COUNT(t.id) FROM Til t WHERE t.user.id=:id and t.expired=false")
    Long ingTil(@Param("id") Long userId);

    @Query("SELECT COUNT(t.id) FROM Til t WHERE t.user.id=:id and t.expired=true and t.crtCnt>=t.totalCnt")
    Long succTil(@Param("id") Long userId);

    @Query("SELECT COUNT(t.id) FROM Til t WHERE t.user.id=:id and t.expired=true and t.crtCnt<t.totalCnt")
    Long failTil(@Param("id") Long userId);
}
