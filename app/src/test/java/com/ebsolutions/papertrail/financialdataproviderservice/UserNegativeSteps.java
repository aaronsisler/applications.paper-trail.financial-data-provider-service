package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ServerError;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserNegativeSteps extends BaseTest {

  protected final UserRepository userRepository;

  protected MvcResult result;

  @And("no users exist")
  public void noUsersExist() {
    when(userRepository.findAll()).thenReturn(Collections.emptyList());
  }

  @Then("the correct empty users response is returned")
  public void theCorrectEmptyUsersResponseIsReturned() throws UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @And("the connection to the database fails")
  public void theConnectionToTheDatabaseFails() {
    DataProcessingException dataProcessingException =
        new DataProcessingException("Generic Exception Message!");

    when(userRepository.findAll()).thenThrow(dataProcessingException);
  }

  @Then("the correct failure response is returned")
  public void theCorrectFailureResponseIsReturned()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ServerError serverError = objectMapper.readValue(content, ServerError.class);
    Assertions.assertEquals("Generic Exception Message!", serverError.getMessage());
  }

  @When("the get all users endpoint is invoked negative")
  public void theGetAllUsersEndpointIsInvokedNegative() throws Exception {
    result = mockMvc.perform(get(Constants.USERS_URL)).andReturn();
  }
}
