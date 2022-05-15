package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MailRepository extends JpaRepository<Mail, Long> {
}
