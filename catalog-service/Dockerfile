# Stage 1: Download the dependencies
FROM eclipse-temurin:25-alpine-3.23 AS dependencies
RUN apk add --no-cache maven
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

# Stage 2: Build the application
FROM dependencies AS builder
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:25-jre-alpine-3.23 AS runtime
WORKDIR /app
COPY --from=builder /build/target/*.jar bookstore-catalog-service.jar

ENTRYPOINT ["java", "-jar", "bookstore-catalog-service.jar"]
