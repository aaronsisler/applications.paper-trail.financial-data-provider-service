package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import io.cucumber.java.en.Given;

public class ActuatorSteps extends BaseTest {
  @Given("application is up")
  public void applicationIsUp() throws Exception {
    mockMvc.perform(get(Constants.HEALTH_CHECK_URI))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("UP")));
  }
}
