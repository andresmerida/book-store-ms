package dev.am.bookstore.orders.domain;

import static dev.am.bookstore.orders.domain.OrderMapper.mapToEntity;

import dev.am.bookstore.orders.dto.CreateOrderRequest;
import dev.am.bookstore.orders.dto.OrderResponse;
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

    @Override
    public OrderResponse createOrder(String username, CreateOrderRequest orderRequest) {
        OrderEntity newOrderEntity = mapToEntity(orderRequest);
        newOrderEntity.setUserName(username);
        OrderEntity savedOrder = orderRepository.save(newOrderEntity);

        log.info("Order {} created", savedOrder.getOrderNumber());

        return new OrderResponse(savedOrder.getOrderNumber());
    }
}
