package dev.am.bookstore.orders.web.controllers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class RabbitMQListener {

    @RabbitListener(queues = "${app.new-orders-queue}")
    void handleNewOrder(MyPayload payload) {
        IO.println("New Order: " + payload.content());
    }

    @RabbitListener(queues = "${app.delivered-orders-queue}")
    void handleDeliveredOrder(MyPayload payload) {
        IO.println("Delivered Order: " + payload.content());
    }
}
