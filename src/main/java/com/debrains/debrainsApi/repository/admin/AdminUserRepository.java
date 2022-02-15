package com.debrains.debrainsApi.repository.admin;

import com.debrains.debrainsApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository  extends JpaRepository<User, Long> {

    /**
     * 전체 회원 조회
     */
    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Long id);



}
