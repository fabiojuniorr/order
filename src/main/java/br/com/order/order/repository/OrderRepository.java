package br.com.order.order.repository;

import br.com.order.order.dto.enums.OrderStatus;
import br.com.order.order.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

  Optional<Order> findByOrderId(String orderId);
  Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
