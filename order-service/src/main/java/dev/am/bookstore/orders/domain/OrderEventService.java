package dev.am.bookstore.orders.domain;

import static dev.am.bookstore.orders.domain.enums.OrderEventType.ORDER_DELIVERED;
import static dev.am.bookstore.orders.utils.JsonUtil.fromJson;
import static dev.am.bookstore.orders.utils.JsonUtil.toJson;

import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.am.bookstore.orders.domain.enums.OrderEventType;
import dev.am.bookstore.orders.dto.OrderCancelledEvent;
import dev.am.bookstore.orders.dto.OrderCreatedEvent;
import dev.am.bookstore.orders.dto.OrderDeliveredEvent;
import dev.am.bookstore.orders.dto.OrderErrorEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setEventType(ORDER_DELIVERED);
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setCreatedAt(event.createdAt());
        orderEventEntity.setPayload(toJson(jsonMapper, event));

        orderEventRepository.save(orderEventEntity);
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJson(jsonMapper, event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderErrorEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJson(jsonMapper, event));
        this.orderEventRepository.save(orderEvent);
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
            case ORDER_CREATED ->
                orderEventPublisher.publish(fromJson(jsonMapper, event.getPayload(), OrderCreatedEvent.class));
            case ORDER_DELIVERED ->
                orderEventPublisher.publish(fromJson(jsonMapper, event.getPayload(), OrderDeliveredEvent.class));
            case ORDER_CANCELLED ->
                orderEventPublisher.publish(fromJson(jsonMapper, event.getPayload(), OrderCancelledEvent.class));
            case ORDER_PROCESSING_FAILED ->
                orderEventPublisher.publish(fromJson(jsonMapper, event.getPayload(), OrderErrorEvent.class));
            default -> log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}
