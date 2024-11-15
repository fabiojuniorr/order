package br.com.order.order.service.strategy;

import br.com.order.order.model.Item;
import java.math.BigDecimal;
import java.util.List;

public interface PriceCalculatorStrategy {

  BigDecimal calculate(List<Item> items);
}
