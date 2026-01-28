package com.congty9a4.backend.repository.jpa;

import com.congty9a4.backend.entity.Userchan;

import java.util.Optional;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Userchan, UUID> , JpaSpecificationExecutor<Userchan> {
   Optional<Userchan> findByEmail(String email);

    boolean existsByEmail(String email);

    @Override
    Page<Userchan> findAll(@Nullable Specification<Userchan> spec, Pageable pageable);
}

