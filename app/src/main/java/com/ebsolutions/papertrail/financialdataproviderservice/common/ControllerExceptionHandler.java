package com.ebsolutions.papertrail.financialdataproviderservice.common;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ServerError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(DataProcessingException.class)
  public ResponseEntity<?> handleServerError(DataProcessingException dataProcessingException) {
    return ResponseEntity.internalServerError()
        .body(ServerError.builder().message(dataProcessingException.getMessage()).build());
  }
}
