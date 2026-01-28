package com.congty9a4.backend.repository.jpa;

import com.congty9a4.backend.entity.Profile;
import com.congty9a4.backend.entity.Userchan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUser(Userchan user);
    Optional<Profile> findByUserId(java.util.UUID userId);
    Optional<Profile> findByKeyName(String keyName);
    boolean existsByKeyName(String keyName);
}

