package br.com.order.order.kafka;

import br.com.order.order.dto.OrderDto;
import br.com.order.order.model.ErrorHistory;
import br.com.order.order.service.ErrorHistoryService;
import br.com.order.order.service.OrderService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryConsumer {

    private final OrderService orderService;
    private final RetryProducer retryProducer;
    private final ErrorHistoryService errorHistoryService;

    @Value("${kafka.retry.max-attempt}")
    private Integer maxAttempts;

    @KafkaListener(topics = "${kafka.topic.name.retry-processing}")
    public void consumer(OrderDto orderDto) {
        try {
            orderService.process(orderDto);
            log.info("order saved successfully!");
        } catch (Exception e) {
            int retryCount = orderDto.getNumberAttempt() + 1;
            orderDto.setNumberAttempt(retryCount);

            if (retryCount < maxAttempts) {
                log.info("Retry processing - Attempt number {} ", retryCount);
                retryProducer.publish(orderDto);
            } else {
                log.info("Retry processing - It was not possible to retrieve the event, the order {} will be retained in the error history ", orderDto.getOrderId());
                errorHistoryService.save(createErrorHistory(e.getMessage(), orderDto));
            }
        }
    }

    private ErrorHistory createErrorHistory(String message, OrderDto orderDto) {
        return ErrorHistory.builder()
            .orderId(orderDto.getOrderId())
            .createdAt(LocalDateTime.now())
            .cause(message)
            .numAttempt(orderDto.getNumberAttempt())
            .build();
    }
}
