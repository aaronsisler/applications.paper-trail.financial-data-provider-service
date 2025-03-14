package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.model.User;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.UserTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
import org.springframework.web.client.RestClient;

@Slf4j
public class UserSteps extends BaseStep {
  private final List<Integer> newlyCreateUserIds = new ArrayList<>();

  private String requestContent;
  private RestClient.ResponseSpec response;
  private User expectedUserOne;
  private User expectedUserTwo;
  private int resultUserId;
  private String userByIdUrl;
  private User updatedUser;

  @Before
  public void setup() {
    expectedUserOne =
        User.builder()
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    expectedUserTwo = User.builder()
        .username("second_user")
        .firstName("second")
        .lastName("user")
        .build();
  }

  @And("two valid users are part of the request body for the create all users endpoint")
  public void twoValidUsersArePartOfTheRequestBodyForTheCreateAllUsersEndpoint()
      throws JsonProcessingException {
    requestContent =
        objectMapper.writeValueAsString(Arrays.asList(expectedUserOne, expectedUserTwo));
  }

  @When("the create all users endpoint is invoked")
  public void theCreateAllUsersEndpointIsInvoked() {
    response = createUserThroughApi();
  }

  @Then("the newly created users are returned from the create all users endpoint")
  public void theNewlyCreatedUsersAreReturnedFromTheCreateAllUsersEndpoint() {
    List<User> users = response.body(
        new ParameterizedTypeReference<ArrayList<User>>() {
        });

    Assertions.assertNotNull(users);
    Assertions.assertEquals(2, users.size());

    User userOne = users.getFirst();
    UserTestUtil.assertExpectedUserAgainstCreatedUser(expectedUserOne, userOne);
    newlyCreateUserIds.add(userOne.getUserId());

    User userTwo = users.getLast();
    UserTestUtil.assertExpectedUserAgainstCreatedUser(expectedUserTwo, userTwo);
    newlyCreateUserIds.add(userTwo.getUserId());
  }

  @When("the get all users endpoint is invoked")
  public void theGetAllUsersEndpointIsInvoked() {
    response = restClient
        .get()
        .uri(TestConstants.USERS_URI)
        .retrieve();
  }

  @Then("the correct users are returned from the get all users endpoint")
  public void theCorrectUsersAreReturnedFromTheGetAllUsersEndpoint() {
    List<User> users = response.body(
        new ParameterizedTypeReference<ArrayList<User>>() {
        });

    Assertions.assertNotNull(users);
    List<User> createdUsers =
        users.stream().filter(user -> newlyCreateUserIds.contains(user.getUserId())).toList();
    Assertions.assertEquals(2, createdUsers.size());

    User userOne = createdUsers.getFirst();
    UserTestUtil.assertExpectedUserAgainstCreatedUser(expectedUserOne, userOne);

    User userTwo = createdUsers.getLast();
    UserTestUtil.assertExpectedUserAgainstCreatedUser(expectedUserTwo, userTwo);
  }

  @And("the user id provided exists in the database")
  public void theUserIdProvidedExistsInTheDatabase(DataTable dataTable) throws Exception {
    User inputUser = User.builder()
        .username(CommonTestUtil.isEmptyString(dataTable.column(0).getFirst()))
        .firstName(CommonTestUtil.isEmptyString(dataTable.column(1).getFirst()))
        .lastName(CommonTestUtil.isEmptyString(dataTable.column(2).getFirst()))
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

  @And("an update for the user is valid and part of the request body for the update user endpoint")
  public void anUpdateForTheUserIsValidAndPartOfTheRequestBodyForTheUpdateUserEndpoint()
      throws JsonProcessingException {
    updatedUser = User.builder()
        .userId(resultUserId)
        .username("updated_username")
        .firstName("updated_firstName")
        .lastName("updated_lastName")
        .build();

    requestContent =
        objectMapper.writeValueAsString(updatedUser);
  }

  @When("the update user endpoint is invoked")
  public void theUpdateUserEndpointIsInvoked() {
    response = updateUserThroughApi();
  }

  @When("the delete user endpoint is invoked")
  public void theDeleteUserEndpointIsInvoked() throws Exception {
    response = deleteUserThroughApi();
  }

  @Then("the correct response is returned from the delete user endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteUserEndpoint() {
    checkForNoContentStatusCode(response);
  }

  @And("the correct user is deleted")
  public void theCorrectUserIsDeleted() throws Exception {
    response = getUserThroughApi();

    checkForNoContentStatusCode(response);
  }

  @Then("the updated user is returned from the update user endpoint")
  public void theUpdatedUserIsReturnedFromTheUpdateUserEndpoint() {
    User returnedUpdatedUser = response.body(User.class);

    Assertions.assertNotNull(returnedUpdatedUser);

    UserTestUtil.assertExpectedUserAgainstActualUser(updatedUser, returnedUpdatedUser);
  }

  @And("the updated user is correct in the database")
  public void theUpdatedUserIsCorrectInTheDatabase() {
    User retrievedUpdatedUser = response.body(User.class);

    Assertions.assertNotNull(retrievedUpdatedUser);

    UserTestUtil.assertExpectedUserAgainstActualUser(updatedUser, retrievedUpdatedUser);
  }

  private RestClient.ResponseSpec createUserThroughApi() {
    return checkForErrorStatusCodes(restClient
        .post()
        .uri(TestConstants.USERS_URI)
        .accept(MediaType.APPLICATION_JSON)
        .body(requestContent)
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve());
  }

  private RestClient.ResponseSpec updateUserThroughApi() {
    return checkForErrorStatusCodes(restClient
        .put()
        .uri(TestConstants.USERS_URI)
        .accept(MediaType.APPLICATION_JSON)
        .body(requestContent)
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve());
  }

  private RestClient.ResponseSpec getUserThroughApi() {
    return checkForErrorStatusCodes(restClient
        .get()
        .uri(userByIdUrl)
        .retrieve());
  }

  private RestClient.ResponseSpec deleteUserThroughApi() {
    return checkForErrorStatusCodes(restClient
        .delete()
        .uri(userByIdUrl)
        .retrieve());
  }


  private void checkForNoContentStatusCode(RestClient.ResponseSpec response) {
    response
        .onStatus(HttpStatusCode::is2xxSuccessful,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.NO_CONTENT, retResponse.getStatusCode()));
  }

  private RestClient.ResponseSpec checkForErrorStatusCodes(RestClient.ResponseSpec response) {
    return response
        .onStatus(HttpStatusCode::is4xxClientError,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.OK, retResponse.getStatusCode()))
        .onStatus(HttpStatusCode::is5xxServerError,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.OK, retResponse.getStatusCode()));
  }
}