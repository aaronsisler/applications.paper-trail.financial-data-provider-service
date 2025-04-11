package com.ebsolutions.papertrail.financialdataproviderservice.common.exception;

import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
  /**
   * @param constraintViolationException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handle(
      ConstraintViolationException constraintViolationException) {
    Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();

    if (violations.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .messages(
                      Collections.singletonList(
                          "A mandatory field is missing or something went wrong"))
                  .build()
          );
    }

    List<String> messages = new ArrayList<>();

    violations.forEach(violation ->
        messages.add(
            violation.getPropertyPath().toString()
                .concat("::")
                .concat(violation.getMessage()))
    );

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(messages)
                .build()
        );
  }

  /**
   * This will be called when a path param does not match the type in method name
   *
   * @param methodArgumentTypeMismatchException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handle(
      MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(Collections.singletonList(
                    "Invalid parameter type: " + methodArgumentTypeMismatchException.getName()
                ))
                .build()
        );
  }

  /**
   * This will be called when a body field does not meet constraints
   *
   * @param methodArgumentNotValidException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handle(
      MethodArgumentNotValidException methodArgumentNotValidException) {

    if (methodArgumentNotValidException.getFieldError() != null) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .messages(Collections.singletonList(
                      methodArgumentNotValidException.getFieldError().getDefaultMessage()
                  ))
                  .build()
          );
    }

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(Collections.singletonList(
                    "Invalid input for a field"
                ))
                .build()
        );
  }

  /**
   * @param httpMessageNotReadableException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleDateTimeParseException(
      HttpMessageNotReadableException httpMessageNotReadableException) {

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(Collections.singletonList(
                    httpMessageNotReadableException.getMostSpecificCause().getMessage()))
                .build()
        );
  }

  /**
   * This will be called when an input to a service does not pass business validations
   *
   * @param dataConstraintException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(DataConstraintException.class)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  public ResponseEntity<ErrorResponse> handleDataConstraintException(
      DataConstraintException dataConstraintException) {

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(dataConstraintException.getMessages())
                .build()
        );
  }

  /**
   * @param dataProcessingException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(DataProcessingException.class)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "500",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  public ResponseEntity<?> handleDataProcessingException(
      DataProcessingException dataProcessingException) {
    return ResponseEntity.internalServerError()
        .body(ErrorResponse.builder()
            .messages(Collections
                .singletonList(dataProcessingException.getMessage()))
            .build());
  }


  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handleException(
      DataIntegrityViolationException dataIntegrityViolationException) {
    log.error("Error", dataIntegrityViolationException);
    return ResponseEntity.badRequest().body(ErrorResponse.builder()
        .messages(Collections
            .singletonList("A provided field is relationally incorrect"))
        .build()
    );
  }

  /**
   * @param exception caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(
      Exception exception) {
    log.error("You need to see what exception was actually thrown");
    log.error("Error", exception);
    return ResponseEntity.internalServerError().body(exception);
  }
}
