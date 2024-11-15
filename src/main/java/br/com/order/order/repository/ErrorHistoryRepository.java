package br.com.order.order.repository;

import br.com.order.order.model.ErrorHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorHistoryRepository extends MongoRepository<ErrorHistory, String> {

}
