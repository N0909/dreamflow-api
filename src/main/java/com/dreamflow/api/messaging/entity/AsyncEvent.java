package com.dreamflow.api.messaging.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="async_event")
@Getter
@Setter
public class AsyncEvent {
    public AsyncEvent(){}

    public AsyncEvent(UUID eventId, AsyncWorker worker, int attempts, AsyncStatus asyncStatus, Instant createdAt, Instant updatedAt) {
        this.eventId = eventId;
        this.worker = worker;
        this.attempts = attempts;
        this.asyncStatus = asyncStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Column(name="event_id", length = 36, nullable = false)
    private UUID eventId;
    @Column(name="worker", nullable = false)
    @Enumerated(EnumType.STRING)
    private AsyncWorker worker;
    @Column(name="attempts", nullable = false)
    private int attempts;
    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AsyncStatus asyncStatus;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;


}
