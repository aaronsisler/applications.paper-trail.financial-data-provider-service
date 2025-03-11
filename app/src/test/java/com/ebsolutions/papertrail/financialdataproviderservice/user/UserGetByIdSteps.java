package com.ebsolutions.papertrail.financialdataproviderservice.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.UserTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserGetByIdSteps extends BaseTest {
  protected final UserRepository userRepository;
  private final int userId = 1;
  private MvcResult result;
  private User expectedUser;
  private String getUserByIdUrl;


  @And("the requested user exist in the database")
  public void theRequestedUserExistInTheDatabase() {
    expectedUser =
        User.builder()
            .userId(userId)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    when(userRepository.findById((long) userId)).thenReturn(Optional.ofNullable(expectedUser));
  }

  @And("no user for the given id exists in the database")
  public void noUserForTheGivenIdExistsInTheDatabase() {
    // Nothing to do here
  }

  @And("the user id provided in the url is the correct format for the get user by id endpoint")
  public void theUserIdProvidedInTheUrlIsTheCorrectFormatForTheGetUserByIdEndpoint() {
    getUserByIdUrl = Constants.USERS_URI + "/" + userId;
  }

  @And("the user id provided in the url is the incorrect format for the get user by id endpoint")
  public void theUserIdProvidedInTheUrlIsTheIncorrectFormatForTheGetUserByIdEndpoint() {
    String invalidUserId = "abc";
    getUserByIdUrl = Constants.USERS_URI + "/" + invalidUserId;
  }

  @And("the connection to the database fails for the get user by id endpoint")
  public void theConnectionToTheDatabaseFailsForTheGetUserByIdEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    doThrow(dataProcessingException).when(userRepository).findById(any());
  }

  @When("the get user by id endpoint is invoked")
  public void theGetUserByIdEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getUserByIdUrl))
        .andReturn();
  }

  @Then("the correct empty response is returned from the get user by id endpoint")
  public void theCorrectEmptyResponseIsReturnedFromTheGetUserByIdEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct user are returned from the get user by id endpoint")
  public void theCorrectUserAreReturnedFromTheGetUserByIdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    User user = objectMapper.readValue(content, User.class);

    UserTestUtil.assertExpectedAgainstActual(expectedUser, user);

  }

  @Then("the correct failure response is returned from the get user by id endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetUserByIdEndpoint(DataTable dataTable)
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }


}
