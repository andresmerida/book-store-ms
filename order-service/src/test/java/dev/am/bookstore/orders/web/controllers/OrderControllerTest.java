package dev.am.bookstore.orders.web.controllers;

import static dev.am.bookstore.orders.utils.Constants.USERNAME_TEST;
import static org.mockito.Mockito.when;

import dev.am.bookstore.orders.domain.OrderService;
import dev.am.bookstore.orders.domain.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    OrderService orderService;

    @MockitoBean
    SecurityService securityService;

    RestTestClient restTestClient;

    @BeforeEach
    void setup() {
        restTestClient = RestTestClient.bindTo(mockMvc).build();
        when(securityService.getLoginUsername()).thenReturn(USERNAME_TEST);
    }

    @Test
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid() {}
}
