package com.dreamflow.api.messaging.email.implementation;

import com.dreamflow.api.config.rabbitmq.EmailRabbitMQConfig;
import com.dreamflow.api.messaging.email.EmailMessage;
import com.dreamflow.api.messaging.email.interfaces.EmailMessageProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitEmailMessageProducer implements EmailMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public RabbitEmailMessageProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(EmailMessage emailMessage){
        rabbitTemplate.convertAndSend(
                EmailRabbitMQConfig.EXCHANGE,
                EmailRabbitMQConfig.ROUTING_KEY,
                emailMessage
        );
    }
}
