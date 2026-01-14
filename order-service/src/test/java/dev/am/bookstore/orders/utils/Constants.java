package dev.am.bookstore.orders.utils;

public final class Constants {
    public static final String USERNAME_TEST = "userTest";
    public static final String[] VALID_COUNTRIES = {"Bolivia", "Germany"};

    public static final String DOCKER_POSTGRES_IMAGE_NAME = "postgres:18.1-alpine";
    public static final String DOCKER_RABBITMQ_IMAGE_NAME = "rabbitmq:4.2.2-management-alpine";
    public static final String DOCKER_WIREMOCK_IMAGE_NAME = "wiremock/wiremock:3.13.2-alpine";

    private Constants() {}
}
