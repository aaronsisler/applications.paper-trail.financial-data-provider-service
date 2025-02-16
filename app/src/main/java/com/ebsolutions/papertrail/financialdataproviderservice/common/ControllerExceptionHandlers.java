package com.ebsolutions.papertrail.financialdataproviderservice.common;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ServerError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandlers {
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleBadRequest(
      ConstraintViolationException constraintViolationException) {
    Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
    String errorMessage;
    if (!violations.isEmpty()) {
      StringBuilder builder = new StringBuilder();
      violations.forEach(violation -> builder.append(violation.getPropertyPath()).append(" ")
          .append(violation.getMessage()));
      errorMessage = builder.toString();
    } else {
      errorMessage = "ConstraintViolationException occurred.";
    }
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataProcessingException.class)
  public ResponseEntity<?> handleServerError(DataProcessingException dataProcessingException) {
    log.error(("HERE"));

    ServerError serverError =
        ServerError.builder().message(dataProcessingException.getMessage()).build();

    return ResponseEntity.internalServerError().body(serverError);
  }
}
