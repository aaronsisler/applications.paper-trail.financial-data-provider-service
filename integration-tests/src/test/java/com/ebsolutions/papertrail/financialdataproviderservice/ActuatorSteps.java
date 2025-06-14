package com.ebsolutions.papertrail.financialdataproviderservice;

import com.ebsolutions.papertrail.financialdataproviderservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.HealthCheck;
import io.cucumber.java.en.Given;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;

public class ActuatorSteps extends BaseStep {

  @Given("application is up")
  public void applicationIsUp() {
    Instant pollingEnd =
        Instant.now().plusMillis(TestConstants.APPLICATION_START_TIME_WAIT_PERIOD_IN_MILLISECONDS);
    do {
      try {
        Thread.sleep(100);
        if (checkIfApplicationIsUp()) {
          return;
        }
        if (Instant.now().isAfter(pollingEnd)) {
          break;
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted while waiting for condition", e);
      }
    } while (!Instant.now().isAfter(pollingEnd));

    Assertions.fail("Application did not come up in time");
  }

  private boolean checkIfApplicationIsUp() {
    try {
      HealthCheck healthCheck = restClient
          .get()
          .uri(TestConstants.HEALTH_CHECK_URI)
          .retrieve()
          .body(HealthCheck.class);

      if (healthCheck == null) {
        return false;
      }

      return "UP".equals(healthCheck.getStatus());
    } catch (Exception exception) {
      return false;
    }
  }
}
