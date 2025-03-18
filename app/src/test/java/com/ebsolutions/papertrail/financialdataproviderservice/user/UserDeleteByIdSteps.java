package com.ebsolutions.papertrail.financialdataproviderservice.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserDeleteByIdSteps extends BaseTest {
  protected final UserRepository userRepository;
  private final Integer validUserId = 1;
  private String userUrl;
  private MvcResult result;

  @And("the user id provided exists in the database")
  public void theUserIdProvidedExistsInTheDatabase() {
    // Nothing to do here
  }

  @And("the user id provided does not exist in the database")
  public void theUserIdProvidedDoesNotExistInTheDatabase() {
    // Nothing to do here
  }

  @And("the user id provided in the url is the correct format")
  public void theUserIdProvidedInTheUrlIsTheCorrectFormat() {
    userUrl = Constants.USERS_URI + "/" + validUserId;
  }

  @And("the user id provided in the url is the incorrect format")
  public void theUserIdProvidedInTheUrlIsTheIncorrectFormat() {
    String invalidUserId = "abc";
    userUrl = Constants.USERS_URI + "/" + invalidUserId;
  }

  @And("the connection to the database fails for the delete user endpoint")
  public void theConnectionToTheDatabaseFailsForTheDeleteUserEndpoint() {
    doThrow(new DataProcessingException()).when(userRepository).deleteById(any());
  }

  @When("the delete user endpoint is invoked")
  public void theDeleteUserEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(delete(userUrl))
        .andReturn();
  }

  @Then("the correct response is returned from the delete user endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteUserEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @And("the correct user is deleted")
  public void theCorrectUserIsDeleted() {
    Mockito.verify(userRepository).deleteById(validUserId.longValue());
  }

  @Then("the correct failure response is returned from the delete user endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheDeleteUserEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }
}
