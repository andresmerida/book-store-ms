package dev.am.bookstore.orders.web.controllers;

import dev.am.bookstore.orders.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RabbitMQDemoController {
    private final RabbitTemplate rabbitTemplate;
    private final AppProperties appProperties;

    @PostMapping("/publish")
    void publish(@RequestBody MyMessage message) {
        rabbitTemplate.convertAndSend(appProperties.orderEventsExchange(), message.routingKey(), message.payload());
        IO.println("Message published: " + message.payload().content());
    }
}

record MyMessage(String routingKey, MyPayload payload) {}

record MyPayload(String content) {}
