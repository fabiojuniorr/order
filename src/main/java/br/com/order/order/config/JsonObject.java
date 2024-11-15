package br.com.order.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JsonObject {

    @Bean
    public ObjectMapper getInstance() {
        return Jackson2ObjectMapperBuilder.json().build();
    }
}
