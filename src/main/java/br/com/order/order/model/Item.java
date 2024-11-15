package br.com.order.order.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

  private String productId;
  private Integer amount;
  private BigDecimal unitPrice;
}