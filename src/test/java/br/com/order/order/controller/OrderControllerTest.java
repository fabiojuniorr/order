package br.com.order.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.order.order.dto.GetOrdersDto;
import br.com.order.order.dto.enums.OrderStatus;
import br.com.order.order.model.Item;
import br.com.order.order.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService service;

  @Test
  @DisplayName("Should return a paginated list of orders")
  void testGetAllOrders() throws Exception {
    GetOrdersDto orderDto = createGetOrderDto();
    Page<GetOrdersDto> orderPage = new PageImpl<>(Collections.singletonList(orderDto));

    when(service.getAll(eq(OrderStatus.FINALIZED), any(PageRequest.class)))
        .thenReturn(orderPage);

    mockMvc.perform(get("/orders")
            .param("page", "0")
            .param("size", "10")
            .param("status", "FINALIZED")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].orderId").value("P00921"))
        .andExpect(jsonPath("$.content[0].totalValue").value(BigDecimal.valueOf(20)))
        .andExpect(jsonPath("$.content[0].status").value("FINALIZED"));
  }

  @Test
  @DisplayName("Should return a order by ID")
  void testFindOrderById() throws Exception {
    GetOrdersDto orderDto = createGetOrderDto();

    when(service.find(orderDto.getOrderId())).thenReturn(orderDto);

    mockMvc.perform(get("/orders/" + orderDto.getOrderId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.orderId").value("P00921"))
        .andExpect(jsonPath("$.totalValue").value(BigDecimal.valueOf(20)))
        .andExpect(jsonPath("$.status").value("FINALIZED"));
  }

  private GetOrdersDto createGetOrderDto() {
    return GetOrdersDto.builder()
        .orderId("P00921")
        .createdAt(LocalDateTime.now())
        .itens(List.of(Item.builder().productId("1").amount(2).unitPrice(BigDecimal.TEN).build()))
        .status(OrderStatus.FINALIZED)
        .totalValue(BigDecimal.valueOf(20))
        .build();
  }
}
