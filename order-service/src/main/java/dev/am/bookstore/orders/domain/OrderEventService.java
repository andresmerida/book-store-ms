package dev.am.bookstore.orders.domain;

import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.am.bookstore.orders.domain.enums.OrderEventType;
import dev.am.bookstore.orders.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.am.bookstore.orders.utils.JsonUtil.fromJson;
import static dev.am.bookstore.orders.utils.JsonUtil.toJson;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderEventService {
    private final OrderEventRepository orderEventRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final JsonMapper jsonMapper;

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setEventType(OrderEventType.ORDER_CREATED);
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setCreatedAt(event.createdAt());
        orderEventEntity.setPayload(toJson(jsonMapper, event));

        orderEventRepository.save(orderEventEntity);
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        for (OrderEventEntity event : events) {
            publishEvent(event);
            orderEventRepository.delete(event);
        }
    }

    private void publishEvent(OrderEventEntity event) {
        switch (event.getEventType()) {
            case ORDER_CREATED -> {
                OrderCreatedEvent orderCreatedEvent = fromJson(jsonMapper, event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
            }
            default -> log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}
