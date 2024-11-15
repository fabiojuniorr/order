package br.com.order.order.mapper;

import br.com.order.order.dto.OrderDto;
import br.com.order.order.model.Order;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "totalValue", source = "totalValue")
  Order toEntity(OrderDto orderDto, BigDecimal totalValue);

}
