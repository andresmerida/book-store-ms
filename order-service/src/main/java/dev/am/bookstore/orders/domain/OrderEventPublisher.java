package dev.am.bookstore.orders.domain;

import dev.am.bookstore.orders.config.AppProperties;
import dev.am.bookstore.orders.dto.OrderCreatedEvent;
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
        rabbitTemplate.convertAndSend(appProperties.orderEventsExchange(), appProperties.newOrdersQueue(), event);
        log.info("Published event: {}", event);
    }

}
