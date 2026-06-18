package com.ebenezer.modules.user.repository;

import com.ebenezer.modules.user.model.UserEntity;
import com.ebenezer.shared.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    List<UserEntity> findAllByRole(Role role);
    Page<UserEntity> findAllByRole(Role role, Pageable pageable);
}
