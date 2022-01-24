package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
