package dev.am.bookstore.orders.domain;

import static dev.am.bookstore.orders.domain.OrderMapper.mapToEntity;
import static dev.am.bookstore.orders.utils.CommonConstants.DELIVERY_ALLOWED_COUNTRIES;

import dev.am.bookstore.orders.domain.clients.ProductClient;
import dev.am.bookstore.orders.domain.enums.OrderStatus;
import dev.am.bookstore.orders.dto.*;
import dev.am.bookstore.orders.web.exceptions.InvalidOrderException;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderEventService orderEventService;
    private final OrderEventMapper orderEventMapper;

    @Override
    public OrderResponse createOrder(String username, CreateOrderRequest orderRequest) {
        validateOrder(orderRequest);
        OrderEntity newOrderEntity = mapToEntity(orderRequest);
        newOrderEntity.setUserName(username);
        OrderEntity savedOrder = orderRepository.save(newOrderEntity);
        orderEventService.save(orderEventMapper.mapToOrderCreatedEvent(savedOrder));

        log.info("Order {} created", savedOrder.getOrderNumber());
        return new OrderResponse(savedOrder.getOrderNumber());
    }

    public void processNewOrders() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("Processing {} new orders", orders.size());
        for (OrderEntity order : orders) {
            processOrder(order);
        }
    }

    private void processOrder(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                log.info("Order {} can be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(orderEventMapper.mapToOrderDeliveredEvent(order));
            } else {
                log.info("Order {} cannot be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(orderEventMapper.mapToOrderCancelledEvent(
                        order,
                        "Can't deliver to this country "
                                + order.getDeliveryAddress().country()));
            }
        } catch (RuntimeException e) {
            log.error("Failed to process Order with orderNumber: {}", order.getOrderNumber(), e);
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(orderEventMapper.mapToOrderErrorEvent(order, e.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return Stream.of(DELIVERY_ALLOWED_COUNTRIES)
                .anyMatch(country ->
                        country.equalsIgnoreCase(order.getDeliveryAddress().country()));
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
}
