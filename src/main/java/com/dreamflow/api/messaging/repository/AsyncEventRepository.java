package com.dreamflow.api.messaging.repository;

import com.dreamflow.api.messaging.entity.AsyncEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AsyncEventRepository extends JpaRepository<AsyncEvent, UUID> {
    Optional<AsyncEvent> findById(UUID eventId);
}
