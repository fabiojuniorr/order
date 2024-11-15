package br.com.order.order.kafka;

import br.com.order.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryProducer {

	@Value("${kafka.topic.name.retry-processing}")
	private String topicName;

	private final KafkaTemplate<String, OrderDto> kafkaTemplate;

	public void publish(OrderDto orderDto) {
		try {
			log.info("Retry - Sending order {} topic: {}", orderDto.getOrderId(), topicName);

			kafkaTemplate.send(topicName, orderDto);

			log.info("Retry - Order {} successfully sent to topic: {}", orderDto.getOrderId(), topicName);
		} catch (Exception e) {
			log.error("Retry - Unable to send order {} to topic: {}", orderDto.getOrderId(), topicName);
		}
	}
}
