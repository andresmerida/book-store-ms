package dev.am.bookstore.orders.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent (
        String eventId,
        String orderNumber,
        Set<OrderItemRequest> items,
        CustomerRequest customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}
