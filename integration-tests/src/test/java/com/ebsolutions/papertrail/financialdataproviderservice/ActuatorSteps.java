package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ebsolutions.papertrail.financialdataproviderservice.model.HealthCheck;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import io.cucumber.java.en.Given;
import org.junit.jupiter.api.Assertions;

public class ActuatorSteps extends BaseStep {
  @Given("application is up")
  public void applicationIsUp() {

    assertDoesNotThrow(() -> {
      HealthCheck healthCheck = restClient
          .get()
          .uri(TestConstants.HEALTH_CHECK_URI)
          .retrieve()
          .body(HealthCheck.class);

      Assertions.assertNotNull(healthCheck);

      Assertions.assertEquals("UP", healthCheck.getStatus());
    }, "Health Actuator is not up");
  }
}
