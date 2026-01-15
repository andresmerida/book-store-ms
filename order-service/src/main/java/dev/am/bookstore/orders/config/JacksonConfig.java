package dev.am.bookstore.orders.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .findAndAddModules() // Automatically registers modules like JavaTimeModule
                .build();
    }
}
