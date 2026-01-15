package dev.am.bookstore.orders.domain;

import static dev.am.bookstore.orders.domain.OrderMapper.mapToEntity;

import dev.am.bookstore.orders.domain.clients.ProductClient;
import dev.am.bookstore.orders.dto.*;
import dev.am.bookstore.orders.web.exceptions.InvalidOrderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderEventService orderEventService;

    @Override
    public OrderResponse createOrder(String username, CreateOrderRequest orderRequest) {
        validateOrder(orderRequest);
        OrderEntity newOrderEntity = mapToEntity(orderRequest);
        newOrderEntity.setUserName(username);
        OrderEntity savedOrder = orderRepository.save(newOrderEntity);
        orderEventService.save(buildOrderCreatedEvent(savedOrder));

        log.info("Order {} created", savedOrder.getOrderNumber());
        return new OrderResponse(savedOrder.getOrderNumber());
    }

    private void validateOrder(CreateOrderRequest orderRequest) {
        for (OrderItemRequest item : orderRequest.items()) {
            ProductDTO product = productClient.getProductByCode(item.code());
            if (item.price().compareTo(product.price()) != 0) {
                log.error("Invalid order: price of product {} does not match the price in the request", item.code());
                throw new InvalidOrderException("Product price does not match the request price");
            }
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(OrderEntity orderEntity) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                orderEntity.getItems()
                        .stream()
                        .map(orderItemEntity -> new OrderItemRequest(
                                orderItemEntity.getCode(),
                                orderItemEntity.getName(),
                                orderItemEntity.getPrice(),
                                orderItemEntity.getQuantity()
                        ))
                        .collect(Collectors.toSet()),
                new CustomerRequest(
                        orderEntity.getCustomerName(),
                        orderEntity.getCustomerEmail(),
                        orderEntity.getCustomerPhone()
                ),
                orderEntity.getDeliveryAddress(),
                LocalDateTime.now()
        );
    }
}
