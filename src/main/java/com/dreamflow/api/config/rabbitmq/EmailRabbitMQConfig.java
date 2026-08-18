package com.dreamflow.api.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailRabbitMQConfig {
    public static final String QUEUE = "email.queue";
    public static final String EXCHANGE = "email.exchange";
    public static final String ROUTING_KEY = "email.send";

    public static final String EMAIL_DLX = "email.dlx";
    public static final String EMAIL_DLQ = "email.dlq";
    public static final String EMAIL_DLQ_ROUTING_KEY = "email.dead";
    @Bean
    public Queue emailQueue(){
        return QueueBuilder
                .durable(QUEUE)
                .deadLetterExchange(EMAIL_DLX)
                .deadLetterRoutingKey(EMAIL_DLQ_ROUTING_KEY)
                .build();
    }
    @Bean
    public Queue emailDeadlaterQueue(){return QueueBuilder.durable(EMAIL_DLQ).build();}


    @Bean
    public DirectExchange emailExchange(){
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    public DirectExchange emailDeadlaterExchange() { return new DirectExchange(EMAIL_DLX);}

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange){
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(ROUTING_KEY);
    }
    @Bean
    public Binding emailDeadlaterBinding(Queue emailDeadlaterQueue, DirectExchange emailDeadlaterExchange){
        return BindingBuilder
                .bind(emailDeadlaterQueue)
                .to(emailDeadlaterExchange)
                .with(EMAIL_DLQ_ROUTING_KEY);
    }

}
