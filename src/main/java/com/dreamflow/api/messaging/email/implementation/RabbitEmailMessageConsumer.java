package com.dreamflow.api.messaging.email.implementation;

import com.dreamflow.api.config.rabbitmq.EmailRabbitMQConfig;
import com.dreamflow.api.messaging.email.EmailMessage;
import com.dreamflow.api.messaging.email.interfaces.EmailMessageConsumer;
import com.dreamflow.api.messaging.entity.AsyncEvent;
import com.dreamflow.api.messaging.entity.AsyncStatus;
import com.dreamflow.api.messaging.entity.AsyncWorker;
import com.dreamflow.api.messaging.repository.AsyncEventRepository;
import com.dreamflow.api.util.service.email.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Component
public class RabbitEmailMessageConsumer implements EmailMessageConsumer {
    private final EmailService emailService;
    private final AsyncEventRepository asyncEventRepository;


    public RabbitEmailMessageConsumer(EmailService emailService, AsyncEventRepository asyncEventRepository){
        this.emailService = emailService;
        this.asyncEventRepository = asyncEventRepository;
    }

    @RabbitListener(queues = EmailRabbitMQConfig.QUEUE)
    public void consume(EmailMessage emailMessage, Message rabbitMessage){
        AsyncEvent asyncEvent = asyncEventRepository
                .findById(emailMessage.eventId())
                .orElseGet(() ->
                        asyncEventRepository.save(
                                new AsyncEvent(
                                        emailMessage.eventId(),
                                        AsyncWorker.EMAIL,
                                        0,
                                        AsyncStatus.PROCESSING,
                                        Instant.now(),
                                        null
                                )
                        )
                );

        if (asyncEvent.getAsyncStatus()==AsyncStatus.COMPLETED) return;

        try {
            asyncEvent.setAttempts(asyncEvent.getAttempts() + 1);
            asyncEvent.setAsyncStatus(AsyncStatus.PROCESSING);
            emailService.sendMail(
                    emailMessage.to(),
                    emailMessage.subject(),
                    emailMessage.body()
            );
            asyncEvent.setAsyncStatus(AsyncStatus.COMPLETED);
            asyncEvent.setUpdatedAt(Instant.now());
            asyncEventRepository.save(asyncEvent);
        }catch (Exception e){
            asyncEvent.setAsyncStatus(AsyncStatus.FAILED);
            asyncEvent.setUpdatedAt(Instant.now());
            asyncEventRepository.save(asyncEvent);
            throw e;
        }
    }
}
