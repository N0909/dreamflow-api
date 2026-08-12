package com.dreamflow.api.auth.repository;

import com.dreamflow.api.admin.users.dto.AdminUserDTO;
import com.dreamflow.api.auth.entity.Role;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamflow.api.auth.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u WHERE u.email=:email")
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT new com.dreamflow.api.admin.users.dto.AdminUserDTO(u.userId, u.username, u.email, u.role, u.createdAt) FROM User u WHERE u.userId=:userId")
    Optional<AdminUserDTO> findByUserId(int userId);
    @Query("SELECT new com.dreamflow.api.admin.users.dto.AdminUserDTO(u.userId, u.username, u.email, u.role, u.createdAt) FROM User u")
    Page<AdminUserDTO> getUsers(Specification<User> specification,Pageable pageable);
}
