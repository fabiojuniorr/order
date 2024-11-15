package br.com.order.order.service;

import br.com.order.order.model.ErrorHistory;
import br.com.order.order.repository.ErrorHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorHistoryService {

  private final ErrorHistoryRepository errorHistoryRepository;

  public void save(ErrorHistory errorHistory) {
    errorHistoryRepository.save(errorHistory);
  }
}
