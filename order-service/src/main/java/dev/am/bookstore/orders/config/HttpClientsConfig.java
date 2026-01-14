package dev.am.bookstore.orders.config;

import dev.am.bookstore.orders.domain.clients.ProductClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(
        basePackages = "dev.am.bookstore.orders.domain.clients",
        types = ProductClient.class,
        group = "catalog-service")
@RequiredArgsConstructor
class HttpClientsConfig {

    private final AppProperties appProperties;

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer() {
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.simple()
                .withCustomizer(customizer -> {
                    customizer.setConnectTimeout(Duration.ofSeconds(5));
                    customizer.setReadTimeout(Duration.ofSeconds(5));
                })
                .build();

        return groups -> {
            groups.forEachClient((group, builder) -> builder.baseUrl(appProperties.catalogServiceBaseUrl())
                    .requestFactory(requestFactory)
                    .build());
        };
    }
}
