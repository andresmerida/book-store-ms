package dev.am.bookstore.orders.web.controllers;

import dev.am.bookstore.orders.AbstractIntegrationTest;
import dev.am.bookstore.orders.dto.OrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldCreateOrder() {
        String payload = """
                {
                    "customer": {
                        "name": "andres",
                        "email": "amerida@gmail.com",
                        "phone": "591-79724557"
                    },
                    "deliveryAddress": {
                        "addressLine1": "Pasaje Mexico",
                        "addressLine2": "",
                        "city": "Sacaba",
                        "state": "Cochabamba",
                        "zipCode": "50002",
                        "country": "Bolivia"
                    },
                    "items": [
                        {
                            "code": "p100",
                            "name": "Product 1",
                            "price": 25.50,
                            "quantity": 1
                        }
                    ]
                }
                """;
        restTestClient
                .post()
                .uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(OrderResponse.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void shouldReturnBadRequestWhenInvalidPayload() {
        // email and phone missing
        String payload = """
                {
                    "customer": {
                        "name": "andres",
                        "email": "",
                        "phone": ""
                    },
                    "deliveryAddress": {
                        "addressLine1": "Pasaje Mexico",
                        "addressLine2": "",
                        "city": "Sacaba",
                        "state": "Cochabamba",
                        "zipCode": "50002",
                        "country": "Bolivia"
                    },
                    "items": [
                        {
                            "code": "p100",
                            "name": "Product 1",
                            "price": 25.50,
                            "quantity": 1
                        }
                    ]
                }
                """;
        restTestClient
                .post()
                .uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}
