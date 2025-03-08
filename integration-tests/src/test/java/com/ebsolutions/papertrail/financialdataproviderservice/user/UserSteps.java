package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.model.User;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestClient;

@Slf4j
public class UserSteps extends BaseStep {
  private String requestContent;
  private MvcResult result;
  private RestClient.ResponseSpec response;
  private User expectedUserOne;
  private User expectedUserTwo;
  private int resultUserId;
  private String userByIdUrl;

  @Before
  public void setup() {
    expectedUserOne =
        User.builder()
            .userId(1)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    expectedUserTwo = User.builder()
        .userId(2)
        .username("second_user")
        .firstName("second")
        .lastName("user")
        .build();
  }

  @And("two valid users are part of the request body for the create all users endpoint")
  public void twoValidUsersArePartOfTheRequestBodyForTheCreateAllUsersEndpoint()
      throws JsonProcessingException {
    User inputUserOne = User.builder()
        .username("first_user")
        .firstName("first")
        .lastName("user")
        .build();

    User inputUserTwo = User.builder()
        .username("second_user")
        .firstName("second")
        .lastName("user")
        .build();


    requestContent =
        objectMapper.writeValueAsString(Arrays.asList(inputUserOne, inputUserTwo));
  }

  @When("the create all users endpoint is invoked")
  public void theCreateAllUsersEndpointIsInvoked() {
    response = createUserThroughApi();
  }

  @Then("the newly created users are returned from the create all users endpoint")
  public void theNewlyCreatedUsersAreReturnedFromTheCreateAllUsersEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    List<User> users = response.body(
        new ParameterizedTypeReference<ArrayList<User>>() {
        });

    Assertions.assertNotNull(users);
    Assertions.assertEquals(2, users.size());

    User userOne = users.getFirst();
    UserTestUtil.assertExpectedUserAgainstActualUser(expectedUserOne, userOne);

    User userTwo = users.getLast();
    UserTestUtil.assertExpectedUserAgainstActualUser(expectedUserTwo, userTwo);
  }

  @When("the get all users endpoint is invoked")
  public void theGetAllUsersEndpointIsInvoked() throws Exception {
    response = restClient
        .get()
        .uri(TestConstants.USERS_URI)
        .retrieve();
  }

  @Then("the correct users are returned")
  public void theCorrectUsersAreReturned()
      throws UnsupportedEncodingException, JsonProcessingException {
    List<User> users = response.body(
        new ParameterizedTypeReference<ArrayList<User>>() {
        });

    Assertions.assertNotNull(users);
    Assertions.assertEquals(2, users.size());

    User userOne = users.getFirst();
    UserTestUtil.assertExpectedUserAgainstActualUser(expectedUserOne, userOne);

    User userTwo = users.getLast();
    UserTestUtil.assertExpectedUserAgainstActualUser(expectedUserTwo, userTwo);
  }

  @And("the user id provided exists in the database")
  public void theUserIdProvidedExistsInTheDatabase() throws Exception {
    User inputUser = User.builder()
        .username("'get_user_by_id_and_delete_user'")
        .firstName("N/A")
        .lastName("N/A")
        .build();

    requestContent =
        objectMapper.writeValueAsString(Collections.singletonList(inputUser));

    response = createUserThroughApi();

    List<User> users = response.body(
        new ParameterizedTypeReference<ArrayList<User>>() {
        });

    Assertions.assertNotNull(users);
    Assertions.assertEquals(1, users.size());

    User createdUser = users.getFirst();
    UserTestUtil.assertExpectedUserAgainstCreatedUser(inputUser, createdUser);

    Assertions.assertNotNull(createdUser);
    Assertions.assertNotNull(createdUser.getUserId());

    resultUserId = createdUser.getUserId();
    userByIdUrl = TestConstants.USERS_URI + "/" + resultUserId;

    response = getUserThroughApi();

    User retrievedCreatedUser = response.body(User.class);

    Assertions.assertNotNull(retrievedCreatedUser);

    UserTestUtil.assertExpectedUserAgainstActualUser(createdUser, retrievedCreatedUser);
  }

  @And("the user id provided in the url is the correct format")
  public void theUserIdProvidedInTheUrlIsTheCorrectFormat() {
  }

  @When("the delete user endpoint is invoked")
  public void theDeleteUserEndpointIsInvoked() throws Exception {
    //    result = mockMvc
    //        .perform(delete(userByIdUrl))
    //        .andReturn();
  }

  @Then("the correct response is returned from the delete user endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteUserEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @And("the correct user is deleted")
  public void theCorrectUserIsDeleted() throws Exception {
    //    result = mockMvc
    //        .perform(get(userByIdUrl))
    //        .andReturn();

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  private RestClient.ResponseSpec createUserThroughApi() {
    return checkStatusCodes(restClient
        .post()
        .uri(TestConstants.USERS_URI)
        .accept(MediaType.APPLICATION_JSON)
        .body(requestContent)
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve());
  }

  private RestClient.ResponseSpec getUserThroughApi() {
    return checkStatusCodes(restClient
        .get()
        .uri(userByIdUrl)
        .retrieve());
  }

  private RestClient.ResponseSpec checkStatusCodes(RestClient.ResponseSpec response) {
    return response
        .onStatus(HttpStatusCode::is4xxClientError,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.OK, retResponse.getStatusCode()))
        .onStatus(HttpStatusCode::is5xxServerError,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.OK, retResponse.getStatusCode()));
  }
}
