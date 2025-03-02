package com.ebsolutions.papertrail.financialdataproviderservice.common.exception;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class DataConstraintException extends IllegalArgumentException {
  @Serial
  private static final long serialVersionUID = 1L;

  private List<String> messages = new ArrayList<>();

  public DataConstraintException(List<String> errorMessages) {
    this.messages = errorMessages;
  }

  public void addMessage(String message) {
    messages.add(message);
  }
}
