package dev.am.bookstore.orders.domain;

import dev.am.bookstore.orders.config.AppProperties;
import dev.am.bookstore.orders.dto.OrderCancelledEvent;
import dev.am.bookstore.orders.dto.OrderCreatedEvent;
import dev.am.bookstore.orders.dto.OrderDeliveredEvent;
import dev.am.bookstore.orders.dto.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final AppProperties appProperties;

    public void publish(OrderCreatedEvent event) {
        this.send(appProperties.newOrdersQueue(), event);
        log.info("Published OrderCreatedEvent: {}", event);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send(appProperties.deliveredOrdersQueue(), event);
        log.info("Published OrderDeliveredEvent: {}", event);
    }

    public void publish(OrderCancelledEvent event) {
        this.send(appProperties.cancelledOrdersQueue(), event);
        log.info("Published OrderCancelledEvent: {}", event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(appProperties.errorOrdersQueue(), event);
        log.info("Published OrderErrorEvent: {}", event);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(appProperties.orderEventsExchange(), routingKey, payload);
    }
}
