package com.ebsolutions.papertrail.financialdataproviderservice.common.exception;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class DataConstraintException extends IllegalArgumentException {
  @Serial
  private static final long serialVersionUID = 1L;

  private final List<String> errorMessages = new ArrayList<>();

  public DataConstraintException(List<String> errorMessages) {
    this.errorMessages.addAll(errorMessages);
  }
}
