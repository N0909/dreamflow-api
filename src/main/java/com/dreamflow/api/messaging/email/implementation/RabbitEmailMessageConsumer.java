package com.dreamflow.api.messaging.email.implementation;

import com.dreamflow.api.config.rabbitmq.EmailRabbitMQConfig;
import com.dreamflow.api.messaging.email.EmailMessage;
import com.dreamflow.api.messaging.email.interfaces.EmailMessageConsumer;
import com.dreamflow.api.util.service.email.EmailService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Component
public class RabbitEmailMessageConsumer implements EmailMessageConsumer {
    private final EmailService emailService;

    public RabbitEmailMessageConsumer(EmailService emailService){
        this.emailService = emailService;
    }

    @RabbitListener(queues = EmailRabbitMQConfig.QUEUE)
    public void consume(EmailMessage emailMessage, Message rabbitMessage){
        System.out.println(
                "eventId = " + emailMessage.eventId()
                        + ", redelivered = "
                        + rabbitMessage.getMessageProperties().getRedelivered()
        );

//        emailService.sendMail(
//                emailMessage.to(),
//                emailMessage.subject(),
//                emailMessage.body()
//        );

        throw new RuntimeException("Exception");
    }
}
