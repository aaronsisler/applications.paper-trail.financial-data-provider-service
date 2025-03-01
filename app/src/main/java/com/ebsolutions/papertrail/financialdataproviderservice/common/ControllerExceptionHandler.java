package com.ebsolutions.papertrail.financialdataproviderservice.common;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  public ResponseEntity<ErrorResponse> handle(
      ConstraintViolationException constraintViolationException) {
    Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();

    if (violations.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .message("ConstraintViolationException occurred")
                  .build()
          );
    }

    StringBuilder builder = new StringBuilder();

    violations.forEach(violation -> builder
        .append(violation.getPropertyPath())
        .append(" ")
        .append(violation.getMessage()));

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .message(builder.toString())
                .build()
        );
  }

  @ExceptionHandler(DataProcessingException.class)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "500",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  public ResponseEntity<?> handleServerError(
      DataProcessingException dataProcessingException) {
    return ResponseEntity.internalServerError()
        .body(ErrorResponse.builder().message(dataProcessingException.getMessage()).build());
  }
}
