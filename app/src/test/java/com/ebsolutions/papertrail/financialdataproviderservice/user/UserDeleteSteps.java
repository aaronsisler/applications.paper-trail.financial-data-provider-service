package com.ebsolutions.papertrail.financialdataproviderservice.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
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
public class UserDeleteSteps extends BaseTest {
  protected final UserRepository userRepository;

  private MvcResult result;
  private User expectedUserOne;

  @And("two users exist in the database")
  public void twoUsersExistInTheDatabase() {
    expectedUserOne =
        User.builder()
            .userId(1)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();


    when(userRepository.findAll()).thenReturn(Collections.singletonList(expectedUserOne));
  }

  @And("user exist in the database")
  public void userExistInTheDatabase() {
  }

  @And("the connection to the database fails for the delete user endpoint")
  public void theConnectionToTheDatabaseFailsForTheDeleteUserEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(userRepository.findAll()).thenThrow(dataProcessingException);
  }

  @When("the delete user endpoint is invoked")
  public void theDeleteUserEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(delete(Constants.USERS_URI)).andReturn();
  }

  @Then("the correct user is deleted")
  public void theCorrectUserIsDeleted() {
  }

  @And("the correct response is returned from the delete user endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteUserEndpoint() {
  }

  @Then("the correct failure response is returned from the delete user endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheDeleteUserEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while getting all users",
        errorResponse.getMessages().getFirst());
  }
}
