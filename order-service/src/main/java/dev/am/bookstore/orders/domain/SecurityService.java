package dev.am.bookstore.orders.domain;

import org.springframework.stereotype.Component;

@Component
public class SecurityService {
    public String getLoginUsername() {
        return "user";
    }
}
