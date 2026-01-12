package dev.am.bookstore.orders.dto;

import dev.am.bookstore.orders.domain.enums.OrderStatus;

public record OrderSummary(String orderNumber, OrderStatus status) {}
