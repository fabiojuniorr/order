package br.com.order.order.kafka;

import br.com.order.order.config.JsonObject;
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

	private final JsonObject jsonObject;
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void publish(Order order) {
		try {
			log.info("Sending order {} to topic: {}", order.getOrderId(), topicName);

			String json = jsonObject.getInstance().writeValueAsString(order);
			kafkaTemplate.send(topicName, json);

			log.info("Product {} successfully sent to topic: {}", order.getOrderId(), topicName);
		} catch (Exception e) {
			log.error("Unable to send product {} to topic: {}", order.getOrderId(), topicName);
		}
	}
}
