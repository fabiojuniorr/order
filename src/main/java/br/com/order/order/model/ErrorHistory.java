package br.com.order.order.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "erro-history")
public class ErrorHistory {

  @Id
  private String id;
  private Long orderId;
  private Integer numAttempt;
  private LocalDateTime createdAt;
  private String cause;
  private String message;
}
