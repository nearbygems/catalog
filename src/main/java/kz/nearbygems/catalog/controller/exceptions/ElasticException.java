package kz.nearbygems.catalog.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ElasticException extends Exception {

  public ElasticException(String message) {
    super(message);
  }

}
