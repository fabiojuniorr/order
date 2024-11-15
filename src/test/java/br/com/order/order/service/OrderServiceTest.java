package br.com.order.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.order.order.dto.GetOrdersDto;
import br.com.order.order.dto.OrderDto;
import br.com.order.order.dto.enums.OrderStatus;
import br.com.order.order.exception.NotFoundException;
import br.com.order.order.exception.OrderException;
import br.com.order.order.kafka.OrderProducer;
import br.com.order.order.kafka.RetryProducer;
import br.com.order.order.mapper.OrderMapperImpl;
import br.com.order.order.model.Item;
import br.com.order.order.model.Order;
import br.com.order.order.repository.OrderRepository;
import br.com.order.order.service.strategy.PriceCalculator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @InjectMocks
  private OrderService service;

  @Mock
  private OrderRepository repository;

  @Mock
  private OrderProducer orderProducer;

  @Mock
  private RetryProducer retryProducer;

  @BeforeEach
  void setUp() {
    service = new OrderService(new OrderMapperImpl(), repository, orderProducer, retryProducer);
  }

  @Test
  @DisplayName("should process an order successfully")
  void shouldProcessOrderSuccessfully() {
    OrderDto orderDto = createOrderDtoMock();
    Order order = createOrderMock(orderDto);

    when(repository.findByOrderId(orderDto.getOrderId()))
        .thenReturn(Optional.empty());

    service.orderProcess(orderDto);

    verify(repository, times(1)).save(order);
    verify(orderProducer, times(1)).publish(order);
  }

  @Test
  @DisplayName("should retry processing order when an exception occurs")
  void shouldRetryProcessingOrderOnError() {
    OrderDto orderDto = createOrderDtoMock();

    when(repository.findByOrderId(orderDto.getOrderId())).thenThrow(new RuntimeException("Database error"));

    service.orderProcess(orderDto);

    verify(retryProducer, times(1)).publish(orderDto);
  }

  @Test
  @DisplayName("should throw exception when order already exists")
  void shouldThrowExceptionWhenOrderAlreadyExists() {
    OrderDto orderDto = createOrderDtoMock();
    Order order = createOrderMock(orderDto);

    when(repository.findByOrderId(orderDto.getOrderId()))
        .thenReturn(Optional.of(order));

    when(repository.findByOrderId(orderDto.getOrderId()))
        .thenReturn(Optional.of(order));

    OrderException exception = assertThrows(OrderException.class, () -> service.process(orderDto));

    assertEquals("Order 123 already registered", exception.getMessage());
  }

  @Test
  @DisplayName("should calculate total price of items")
  void shouldCalculateTotalPrice() {
    OrderDto orderDto = createOrderDtoMock();
    BigDecimal totalValue = new PriceCalculator().calculate(orderDto.getItens());
    assertEquals(BigDecimal.valueOf(20), totalValue);
  }

  @Test
  @DisplayName("should throw exception for invalid order date")
  void shouldThrowExceptionForInvalidOrderDate() {
    LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
    OrderDto orderDto = createOrderDtoMock();
    orderDto.setCreatedAt(pastDate);

    OrderException exception = assertThrows(OrderException.class, () -> service.process(orderDto));

    assertEquals("The order date must be the current date or a future date.", exception.getMessage());
  }

  @Test
  @DisplayName("should find an order by ID")
  void shouldFindOrderById() {
    String orderId = "123";
    OrderDto orderDto = createOrderDtoMock();
    Order order = createOrderMock(orderDto);

    when(repository.findByOrderId(orderId))
        .thenReturn(Optional.of(order));

    GetOrdersDto result = service.find(orderId);
    assertNotNull(result);
    assertEquals(orderId, result.getOrderId());
  }

  private GetOrdersDto createGetOrderDto(Order order) {
    return GetOrdersDto.builder()
        .orderId(order.getOrderId())
        .createdAt(order.getCreatedAt())
        .itens(order.getItens())
        .status(order.getStatus())
        .totalValue(order.getTotalValue())
        .build();
  }

  @Test
  @DisplayName("should throw exception when order not found")
  void shouldThrowExceptionWhenOrderNotFound() {
    String orderId = "123";

    when(repository.findByOrderId(orderId))
        .thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> service.find(orderId));

    assertEquals("Order not found.", exception.getMessage());
  }

  @Test
  @DisplayName("should return paginated orders by status")
  void shouldReturnPaginatedOrdersByStatus() {
    OrderDto orderDto = createOrderDtoMock();
    Order order = createOrderMock(orderDto);

    PageRequest pageRequest = PageRequest.of(0, 10);
    OrderStatus status = OrderStatus.FINALIZED;

    List<Order> orders = List.of(order);
    Page<Order> page = new PageImpl<>(orders);

    when(repository.findByStatus(status, pageRequest))
        .thenReturn(page);

    Page<GetOrdersDto> result = service.getAll(status, pageRequest);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
  }

  private Order createOrderMock(OrderDto orderDto) {
    return Order.builder()
        .orderId(orderDto.getOrderId())
        .status(OrderStatus.FINALIZED)
        .createdAt(orderDto.getCreatedAt())
        .itens(orderDto.getItens())
        .totalValue(BigDecimal.valueOf(20))
        .build();
  }

  private OrderDto createOrderDtoMock() {
    return OrderDto.builder()
        .orderId("123")
        .createdAt(LocalDateTime.now().plusHours(1))
        .status(OrderStatus.PENDING)
        .itens(List.of(Item.builder().productId("1").amount(2).unitPrice(BigDecimal.TEN).build()))
        .build();
  }
}
