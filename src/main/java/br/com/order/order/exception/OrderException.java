package br.com.order.order.exception;

public class OrderException extends RuntimeException {

  public OrderException(String message) {
    super(message);
  }

  public OrderException(String message, Throwable cause) {
    super(message, cause);
  }
}