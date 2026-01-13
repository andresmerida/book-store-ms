package dev.am.bookstore.orders.web.controllers;

import static dev.am.bookstore.orders.utils.Constants.USERNAME_TEST;
import static dev.am.bookstore.orders.utils.Constants.VALID_COUNTRIES;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.am.bookstore.orders.domain.OrderService;
import dev.am.bookstore.orders.domain.SecurityService;
import dev.am.bookstore.orders.dto.Address;
import dev.am.bookstore.orders.dto.CreateOrderRequest;
import dev.am.bookstore.orders.dto.CustomerRequest;
import dev.am.bookstore.orders.dto.OrderItemRequest;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JsonMapper jsonMapper;

    @MockitoBean
    OrderService orderService;

    @MockitoBean
    SecurityService securityService;

    @BeforeEach
    void setup() {
        when(securityService.getLoginUsername()).thenReturn(USERNAME_TEST);
    }

    @ParameterizedTest(name = "[{index}-{0}]")
    @MethodSource("createOrderRequestProviderWithBadRequests")
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request) throws Exception {
        when(orderService.createOrder(eq(USERNAME_TEST), any(CreateOrderRequest.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> createOrderRequestProviderWithBadRequests() {
        Set<OrderItemRequest> validOrderItems =
                Set.of(new OrderItemRequest("P100", "Product 1", new BigDecimal("25.50"), 1));
        return Stream.of(
                arguments(named(
                        "Order with invalid customer",
                        Instancio.of(CreateOrderRequest.class)
                                .generate(field(CustomerRequest::email), gen -> gen.text()
                                        .pattern("#a#a#a#a#a#a@mail.com"))
                                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                                .set(field(CustomerRequest::phone), "")
                                .set(field(CreateOrderRequest::items), validOrderItems)
                                .create())),
                arguments(named(
                        "Order with no items",
                        Instancio.of(CreateOrderRequest.class)
                                .generate(field(CustomerRequest::email), gen -> gen.text()
                                        .pattern("#a#a#a#a#a#a@mail.com"))
                                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                                .set(field(CreateOrderRequest::items), Set.of())
                                .create())),
                arguments(named(
                        "Order with invalid delivery address",
                        Instancio.of(CreateOrderRequest.class)
                                .generate(field(CustomerRequest::email), gen -> gen.text()
                                        .pattern("#c#c#c#c#d#d@mail.com"))
                                .set(field(Address::country), "")
                                .set(field(CreateOrderRequest::items), validOrderItems)
                                .create())));
    }
}
