package com.ebsolutions.papertrail.financialdataproviderservice.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.UserTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserGetAllSteps extends BaseTest {
  protected final UserRepository userRepository;

  private MvcResult result;
  private User expectedUserOne;
  private User expectedUserTwo;

  @And("two users exist in the database")
  public void twoUsersExistInTheDatabase() {
    expectedUserOne =
        User.builder()
            .id(1)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    expectedUserTwo =
        User.builder()
            .id(2)
            .username("second_user")
            .firstName("second")
            .lastName("user")
            .build();

    when(userRepository.findAll()).thenReturn(Arrays.asList(expectedUserOne, expectedUserTwo));
  }

  @And("no users exist")
  public void noUsersExist() {
    when(userRepository.findAll()).thenReturn(Collections.emptyList());
  }

  @And("the connection to the database fails for the get all users endpoint")
  public void theConnectionToTheDatabaseFailsForTheGetAllUsersEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(userRepository.findAll()).thenThrow(dataProcessingException);
  }

  @When("the get all users endpoint is invoked")
  public void theGetAllUsersEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(get(Constants.USERS_URI)).andReturn();
  }

  @Then("the correct users are returned")
  public void theCorrectUsersAreReturned()
      throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<User> users = objectMapper.readerForListOf(User.class).readValue(content);

    User userOne = users.getFirst();
    UserTestUtil.assertExpectedAgainstActual(expectedUserOne, userOne);

    User userTwo = users.getLast();
    UserTestUtil.assertExpectedAgainstActual(expectedUserTwo, userTwo);
  }

  @Then("the correct empty users response is returned")
  public void theCorrectEmptyUsersResponseIsReturned() throws UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct failure response is returned from the get all users endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAllUsersEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while fetching all users",
        errorResponse.getMessages().getFirst());
  }
}
