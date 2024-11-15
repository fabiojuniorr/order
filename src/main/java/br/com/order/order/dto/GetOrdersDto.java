package br.com.order.order.dto;

import br.com.order.order.dto.enums.OrderStatus;
import br.com.order.order.model.Item;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetOrdersDto {

  private String orderId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  private OrderStatus status;

  private List<Item> itens;

  private BigDecimal totalValue;
}
