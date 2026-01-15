package dev.am.bookstore.orders.domain;

import dev.am.bookstore.orders.dto.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
class OrderEventMapper {

    OrderCreatedEvent mapToOrderCreatedEvent(OrderEntity entity) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                entity.getOrderNumber(),
                mapItems(entity),
                mapCustomer(entity),
                entity.getDeliveryAddress(),
                LocalDateTime.now());
    }

    OrderDeliveredEvent mapToOrderDeliveredEvent(OrderEntity entity) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                entity.getOrderNumber(),
                mapItems(entity),
                mapCustomer(entity),
                entity.getDeliveryAddress(),
                LocalDateTime.now());
    }

    OrderCancelledEvent mapToOrderCancelledEvent(OrderEntity entity, String reason) {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                entity.getOrderNumber(),
                mapItems(entity),
                mapCustomer(entity),
                entity.getDeliveryAddress(),
                reason,
                LocalDateTime.now());
    }

    OrderErrorEvent mapToOrderErrorEvent(OrderEntity entity, String reason) {
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                entity.getOrderNumber(),
                mapItems(entity),
                mapCustomer(entity),
                entity.getDeliveryAddress(),
                reason,
                LocalDateTime.now());
    }

    private Set<OrderItemRequest> mapItems(OrderEntity entity) {
        return entity.getItems().stream().map(this::mapItem).collect(Collectors.toSet());
    }

    private OrderItemRequest mapItem(OrderItemEntity item) {
        return new OrderItemRequest(item.getCode(), item.getName(), item.getPrice(), item.getQuantity());
    }

    private CustomerRequest mapCustomer(OrderEntity entity) {
        return new CustomerRequest(entity.getCustomerName(), entity.getCustomerEmail(), entity.getCustomerPhone());
    }
}
