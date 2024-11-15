package br.com.order.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderRetryTopic() {
        return new NewTopic("retry-processing", 3, (short) 1);
    }

    @Bean
    public NewTopic orderProcessingTopic() {
        return new NewTopic("order-processing", 3, (short) 1);
    }

    @Bean
    public NewTopic orderProcessedTopic() {
        return new NewTopic("order-processed", 3, (short) 1);
    }
}
