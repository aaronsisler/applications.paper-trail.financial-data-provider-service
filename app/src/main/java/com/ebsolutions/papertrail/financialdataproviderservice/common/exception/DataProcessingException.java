package com.ebsolutions.papertrail.financialdataproviderservice.common.exception;

import java.io.Serial;

public class DataProcessingException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public DataProcessingException() {
    super();
  }

  public DataProcessingException(String errorMessage) {
    super(errorMessage);
  }
}
