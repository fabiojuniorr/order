package br.com.order.order.repository;

import br.com.order.order.model.Order;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

  Optional<Object> findByOrderId(String orderId);
}
