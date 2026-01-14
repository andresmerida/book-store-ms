package dev.am.bookstore.orders;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static dev.am.bookstore.orders.utils.Constants.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    static WireMockContainer wireMockServerContainer = new WireMockContainer(DOCKER_WIREMOCK_IMAGE_NAME);

    @Bean
    WireMockContainer wireMockContainer() {
        wireMockServerContainer.start();
        configureFor(wireMockServerContainer.getHost(), wireMockServerContainer.getPort());
        return wireMockServerContainer;
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer(DockerImageName.parse(DOCKER_POSTGRES_IMAGE_NAME));
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse(DOCKER_RABBITMQ_IMAGE_NAME));
    }

    @Bean
    DynamicPropertyRegistrar dynamicPropertyRegistrar(WireMockContainer wireMockContainer) {
        return registry -> registry.add("orders.catalog-service-url", wireMockContainer::getBaseUrl);
    }
}
