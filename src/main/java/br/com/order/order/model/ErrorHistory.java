package br.com.order.order.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "erro-history")
public class ErrorHistory {

  @Id
  private String id;
  private String orderId;
  private Integer numAttempt;
  private LocalDateTime createdAt;
  private String cause;
  private String message;
}
