package com.dreamflow.api.messaging.email.interfaces;

import com.dreamflow.api.messaging.email.EmailMessage;
import org.springframework.amqp.core.Message;

public interface EmailMessageConsumer {
    void consume(EmailMessage emailMessage, Message rabbitMessage);
}
