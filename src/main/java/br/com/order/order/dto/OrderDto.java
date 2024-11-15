package br.com.order.order.dto;

import br.com.order.order.dto.enums.OrderStatus;
import br.com.order.order.model.Item;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

  @NotNull(message = "Client ID cannot be null.")
  private String orderId;

  @NotNull(message = "The order date cannot be null.")
  private LocalDateTime createdAt;

  @NotNull(message = "Order status cannot be null.")
  private OrderStatus status;

  @NotEmpty(message = "The item list cannot be empty.")
  private List<Item> itens;

  private int numberAttempt;
}
