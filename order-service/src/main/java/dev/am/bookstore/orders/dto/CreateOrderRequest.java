package dev.am.bookstore.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record CreateOrderRequest(
        @Valid @NotEmpty(message = "Items cannot be empty") Set<OrderItemRequest> items,
        @Valid CustomerRequest customer,
        @Valid Address deliveryAddress) {}
