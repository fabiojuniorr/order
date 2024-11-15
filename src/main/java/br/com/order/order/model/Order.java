package br.com.order.order.model;

import br.com.order.order.dto.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Getter
@Setter
@Document(collection = "order")
public class Order {

    @Id
    private String id;
    
    private String orderId;
    
    private LocalDateTime createdAt;

    @Field(targetType = FieldType.STRING)
    private OrderStatus status;
    
    private List<Item> itens;

    private BigDecimal totalValue;
}