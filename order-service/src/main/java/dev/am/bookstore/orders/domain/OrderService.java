package dev.am.bookstore.orders.domain;

import dev.am.bookstore.orders.dto.CreateOrderRequest;
import dev.am.bookstore.orders.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(String username, CreateOrderRequest orderRequest);
}
