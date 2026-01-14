package dev.am.bookstore.orders.web.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import dev.am.bookstore.orders.AbstractIntegrationTest;
import dev.am.bookstore.orders.dto.OrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class OrderControllerIT extends AbstractIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateOrder() {
        mockGetProductByCode();
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
                            "code": "P100",
                            "name": "The Hunger Games",
                            "price": 34.0,
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
                            "code": "P100",
                            "name": "The Hunger Games",
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

    private void mockGetProductByCode() {
        stubFor(WireMock.get(urlMatching("/api/products/P100"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody("""
                                    {
                                        "code": "P100",
                                        "name": "The Hunger Games",
                                        "price": 34.0
                                    }
                                """)));
    }
}
