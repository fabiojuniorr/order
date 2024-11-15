package br.com.order.order.service.strategy;

import br.com.order.order.model.Item;
import java.math.BigDecimal;
import java.util.List;

public class PriceCalculator implements PriceCalculatorStrategy {

  @Override
  public BigDecimal calculate(List<Item> items) {
    return items.stream()
        .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getAmount())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
