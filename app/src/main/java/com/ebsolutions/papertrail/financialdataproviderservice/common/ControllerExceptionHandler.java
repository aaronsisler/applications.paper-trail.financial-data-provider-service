package com.ebsolutions.papertrail.financialdataproviderservice.common;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ServerError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(DataProcessingException.class)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "500",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ServerError.class))
          }),
  })
  public ResponseEntity<?> handleServerError(DataProcessingException dataProcessingException) {
    return ResponseEntity.internalServerError()
        .body(ServerError.builder().message(dataProcessingException.getMessage()).build());
  }
}
