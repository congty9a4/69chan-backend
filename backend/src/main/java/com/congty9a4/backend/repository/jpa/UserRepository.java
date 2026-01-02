package com.congty9a4.backend.repository.jpa;

import com.congty9a4.backend.config.TrackExecutionTime;
import com.congty9a4.backend.entity.relational.Userchan;

import java.util.Optional;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Userchan, UUID> {

}
