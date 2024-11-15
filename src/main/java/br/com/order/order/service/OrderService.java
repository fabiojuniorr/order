package br.com.order.order.service;

import br.com.order.order.dto.GetOrdersDto;
import br.com.order.order.dto.OrderDto;
import br.com.order.order.dto.enums.OrderStatus;
import br.com.order.order.exception.NotFoundException;
import br.com.order.order.exception.OrderException;
import br.com.order.order.kafka.OrderProducer;
import br.com.order.order.kafka.RetryProducer;
import br.com.order.order.mapper.OrderMapper;
import br.com.order.order.model.Item;
import br.com.order.order.model.Order;
import br.com.order.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderMapper mapper;
  private final OrderRepository repository;
  private final OrderProducer orderProducer;
  private final RetryProducer retryProducer;

  public void orderProcess(OrderDto orderDto) {
    try {
      Order order = process(orderDto);
      orderProducer.publish(order);
    } catch (OrderException e) {
      log.error(e.getMessage());
    } catch (Exception e) {
      log.error("Error processing order {}", orderDto.getOrderId(), e);
      retryProducer.publish(orderDto);
    }
  }

  public Order process(OrderDto orderDto) {
    repository.findByOrderId(orderDto.getOrderId())
        .ifPresent((it) -> {
          throw new OrderException("Order %s already registered".formatted(orderDto.getOrderId()));
        });

    validateOrderDate(orderDto.getCreatedAt());
    BigDecimal totalValue = calculateTotalPrice(orderDto.getItens());
    Order order = mapper.toEntity(orderDto, totalValue, OrderStatus.FINALIZED);

    repository.save(order);
    return order;
  }

  private void validateOrderDate(LocalDateTime dataPedido) {
    if (dataPedido != null && dataPedido.isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException(
          "The order date must be the current date or a future date.");
    }
  }

  public BigDecimal calculateTotalPrice(List<Item> itens) {
    return itens.stream()
        .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getAmount())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Page<GetOrdersDto> getAll(OrderStatus status, Pageable pageable) {
    List<GetOrdersDto> orders = repository.findByStatus(status, pageable).getContent()
        .stream()
        .map(mapper::orderToGetOrderDto).toList();
    return new PageImpl<>(orders, pageable, orders.size());
  }

  public GetOrdersDto find(String orderId) {
    Order order = repository.findByOrderId(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found."));
    return mapper.orderToGetOrderDto(order);
  }
}
