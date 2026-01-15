package dev.am.bookstore.orders.jobs;

import dev.am.bookstore.orders.domain.OrderEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderEventsPublishingJob {
    private final OrderEventService orderEventService;

    @Scheduled(cron = "${app.publish-order-events-job-cron}")
    public void publishOrderEvents() {
        log.info("Publishing order events at: {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
