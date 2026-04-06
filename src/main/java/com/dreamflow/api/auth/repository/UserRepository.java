package com.dreamflow.api.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamflow.api.auth.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email=:email")
    Optional<User> findByEmail(String email);
}
