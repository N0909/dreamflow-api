package com.dreamflow.api.messaging.email;

import java.util.UUID;

public record EmailMessage(UUID eventId, String to, String subject, String body) {
}
