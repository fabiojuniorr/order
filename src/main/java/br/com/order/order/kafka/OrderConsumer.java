package br.com.order.order.kafka;

import br.com.order.order.dto.OrderDto;
import br.com.order.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

	private final OrderService service;

	@KafkaListener(topics = "${kafka.topic.name.order-processing}")
	public void consume(@Valid OrderDto orderDto,
						@Header(KafkaHeaders.OFFSET) String offSet,
						@Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
						@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

		log.info(String.format("Receiving request offset: %s, partition: %s, topic: %s", offSet, partition, topic));
		service.orderProcess(orderDto);
		log.info("End of order reception");
	}
}
