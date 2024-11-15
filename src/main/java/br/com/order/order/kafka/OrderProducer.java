package br.com.order.order.kafka;

import br.com.order.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

	@Value("${kafka.topic.name.order-processed}")
	private String topicName;

	private final KafkaTemplate<String, Order> kafkaTemplate;

	public void publish(Order order) {
		try {
			log.info("Order Producer - Sending order {} to topic: {}", order.getOrderId(), topicName);

			kafkaTemplate.send(topicName, order);

			log.info("Order Producer - Order {} successfully sent to topic: {}", order.getOrderId(), topicName);
		} catch (Exception e) {
			log.error("Order Producer - Unable to send order {} to topic: {}", order.getOrderId(), topicName);
		}
	}
}
