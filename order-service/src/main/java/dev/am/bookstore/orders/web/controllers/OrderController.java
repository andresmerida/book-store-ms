package dev.am.bookstore.orders.web.controllers;

import dev.am.bookstore.orders.domain.OrderService;
import dev.am.bookstore.orders.domain.SecurityService;
import dev.am.bookstore.orders.dto.CreateOrderRequest;
import dev.am.bookstore.orders.dto.OrderResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
class OrderController {
    private final OrderService orderService;
    private final SecurityService securityService;

    @PostMapping
    ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        String username = securityService.getLoginUsername();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(username)
                .toUri();
        log.info("Creating order for user {}", username);

        return ResponseEntity.created(location).body(orderService.createOrder(username, orderRequest));
    }
}
