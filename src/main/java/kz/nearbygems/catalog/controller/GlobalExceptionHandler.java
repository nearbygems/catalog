package kz.nearbygems.catalog.controller;

import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<Void> handleNotFoundException() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(ElasticException.class)
  protected ResponseEntity<Void> handleElasticException() {
    return ResponseEntity.internalServerError().build();
  }

}
