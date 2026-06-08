package com.congty9a4.backend.repository.jpa;

import com.congty9a4.backend.entity.Userchan;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Userchan, UUID>, JpaSpecificationExecutor<Userchan> {
    Optional<Userchan> findByEmail(String email);

    boolean existsByEmail(String email);

    @Override
    Page<Userchan> findAll(@Nullable Specification<Userchan> spec, Pageable pageable);

    List<Userchan> findByIdIn(List<UUID> ids);


    List<Userchan> findAllByUsernameStartingWith(String prefix);

    void deleteAllByUsernameStartingWith(String prefix);
}
