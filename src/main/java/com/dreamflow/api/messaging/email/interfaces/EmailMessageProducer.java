package com.dreamflow.api.messaging.email.interfaces;

import com.dreamflow.api.messaging.email.EmailMessage;

public interface EmailMessageProducer {
    void publish(EmailMessage emailMessage);
}
