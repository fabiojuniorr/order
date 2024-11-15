package br.com.order.order.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

  private String productId;
  private Integer amount;
  private BigDecimal unitPrice;
}