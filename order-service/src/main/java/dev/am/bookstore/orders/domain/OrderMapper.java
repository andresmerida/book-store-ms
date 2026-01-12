package dev.am.bookstore.orders.domain;

import dev.am.bookstore.orders.domain.enums.OrderStatus;
import dev.am.bookstore.orders.dto.CreateOrderRequest;
import dev.am.bookstore.orders.dto.OrderItemRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

final class OrderMapper {
    private OrderMapper() {}

    static OrderEntity mapToEntity(CreateOrderRequest orderRequest) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderNumber(UUID.randomUUID().toString());
        entity.setStatus(OrderStatus.NEW);
        entity.setCustomerName(orderRequest.customer().name());
        entity.setCustomerEmail(orderRequest.customer().email());
        entity.setCustomerPhone(orderRequest.customer().phone());
        entity.setDeliveryAddress(orderRequest.deliveryAddress());

        Set<OrderItemEntity> items = new HashSet<>();
        for (OrderItemRequest item : orderRequest.items()) {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setName(item.name());
            itemEntity.setCode(item.code());
            itemEntity.setPrice(item.price());
            itemEntity.setQuantity(item.quantity());
            itemEntity.setOrder(entity);

            items.add(itemEntity);
        }
        entity.setItems(items);

        return entity;
    }
}
