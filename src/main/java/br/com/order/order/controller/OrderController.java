package br.com.order.order.controller;

import br.com.order.order.dto.GetOrdersDto;
import br.com.order.order.dto.enums.OrderStatus;
import br.com.order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService service;

  @GetMapping
  public ResponseEntity<Page<GetOrdersDto>> getAll(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam OrderStatus status) {

    return ResponseEntity.ok(service.getAll(status, PageRequest.of(page, size)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetOrdersDto> find(@PathVariable String id) {
    return ResponseEntity.ok(service.find(id));
  }
}
