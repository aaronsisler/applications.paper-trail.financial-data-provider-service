package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class UserSteps {
  @Autowired
  protected MockMvc mockMvc;

  protected MvcResult result;

  @And("no users exist")
  public void noUsersExist() {
    // Do nothing for this use case
  }

  @When("the get all users endpoint is invoked")
  public void theGetAllUsersEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(get(Constants.USERS_URL)).andReturn();

  }

  @Then("the correct users response is returned")
  public void theCorrectUsersResponseIsReturned() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }
}
