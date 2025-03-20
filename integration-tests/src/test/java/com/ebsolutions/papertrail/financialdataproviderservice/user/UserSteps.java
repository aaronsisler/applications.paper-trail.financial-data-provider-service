package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.User;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.UserTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.util.ApiCallTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.UserTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
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
    response =
        ApiCallTestUtil.createThroughApi(restClient, TestConstants.USERS_URI, requestContent);
  }

  @Then("the newly created users are returned from the create all users endpoint")
  public void theNewlyCreatedUsersAreReturnedFromTheCreateAllUsersEndpoint() {
    List<User> users = response.body(
        new ParameterizedTypeReference<ArrayList<User>>() {
        });

    Assertions.assertNotNull(users);
    Assertions.assertEquals(2, users.size());

    User userOne = users.getFirst();
    UserTestUtil.assertExpectedAgainstCreated(expectedUserOne, userOne);
    newlyCreateUserIds.add(userOne.getId());

    User userTwo = users.getLast();
    UserTestUtil.assertExpectedAgainstCreated(expectedUserTwo, userTwo);
    newlyCreateUserIds.add(userTwo.getId());
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
        users.stream().filter(user -> newlyCreateUserIds.contains(user.getId())).toList();
    Assertions.assertEquals(2, createdUsers.size());

    User userOne = createdUsers.getFirst();
    UserTestUtil.assertExpectedAgainstCreated(expectedUserOne, userOne);

    User userTwo = createdUsers.getLast();
    UserTestUtil.assertExpectedAgainstCreated(expectedUserTwo, userTwo);
  }

  @And("the user id provided exists in the database")
  public void theUserIdProvidedExistsInTheDatabase() throws Exception {
    User databaseSetupUser = UserTestData.UPDATE.get();

    Assertions.assertNotNull(databaseSetupUser);
    if (databaseSetupUser.getId() == null) {
      Assertions.fail("Data setup failed for user");
    }

    resultUserId = databaseSetupUser.getId();
    userByIdUrl = TestConstants.USERS_URI + "/" + resultUserId;

    response = ApiCallTestUtil.getThroughApi(restClient, userByIdUrl);

    User retrievedCreatedUser = response.body(User.class);

    Assertions.assertNotNull(retrievedCreatedUser);

    UserTestUtil.assertExpectedAgainstActual(databaseSetupUser, retrievedCreatedUser);
  }

  @And("an update for the user is valid and part of the request body for the update user endpoint")
  public void anUpdateForTheUserIsValidAndPartOfTheRequestBodyForTheUpdateUserEndpoint()
      throws JsonProcessingException {
    updatedUser = User.builder()
        .id(resultUserId)
        .username("updated_username")
        .firstName("updated_firstName")
        .lastName("updated_lastName")
        .build();

    requestContent =
        objectMapper.writeValueAsString(updatedUser);
  }

  @And("the update user id provided exists in the database")
  public void theUpdateUserIdProvidedExistsInTheDatabase() {
    User databaseSetupUser = UserTestData.UPDATE.get();

    Assertions.assertNotNull(databaseSetupUser);

    if (databaseSetupUser.getId() == null) {
      Assertions.fail("Data setup failed for user");
    }

    resultUserId = databaseSetupUser.getId();
    userByIdUrl = TestConstants.USERS_URI + "/" + resultUserId;

    response = ApiCallTestUtil.getThroughApi(restClient, userByIdUrl);

    User retrievedCreatedUser = response.body(User.class);

    Assertions.assertNotNull(retrievedCreatedUser);

    UserTestUtil.assertExpectedAgainstActual(databaseSetupUser, retrievedCreatedUser);
  }

  @And("the delete user id provided exists in the database")
  public void theDeleteUserIdProvidedExistsInTheDatabase() {
    User databaseSetupUser = UserTestData.DELETE.get();

    Assertions.assertNotNull(databaseSetupUser);
    if (databaseSetupUser.getId() == null) {
      Assertions.fail("Data setup failed for user");
    }

    resultUserId = databaseSetupUser.getId();
    userByIdUrl = TestConstants.USERS_URI + "/" + resultUserId;

    response = ApiCallTestUtil.getThroughApi(restClient, userByIdUrl);

    User retrievedCreatedUser = response.body(User.class);

    Assertions.assertNotNull(retrievedCreatedUser);

    UserTestUtil.assertExpectedAgainstActual(databaseSetupUser, retrievedCreatedUser);
  }

  @When("the update user endpoint is invoked")
  public void theUpdateUserEndpointIsInvoked() {
    response =
        ApiCallTestUtil.updateThroughApi(restClient, TestConstants.USERS_URI, requestContent);
  }

  @When("the delete user endpoint is invoked")
  public void theDeleteUserEndpointIsInvoked() {
    response = ApiCallTestUtil.deleteThroughApi(restClient, userByIdUrl);
  }

  @Then("the correct response is returned from the delete user endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteUserEndpoint() {
    ApiCallTestUtil.checkForNoContentStatusCode(response);
  }

  @And("the correct user is deleted")
  public void theCorrectUserIsDeleted() {
    response = ApiCallTestUtil.getThroughApi(restClient, userByIdUrl);

    ApiCallTestUtil.checkForNoContentStatusCode(response);
  }

  @Then("the updated user is returned from the update user endpoint")
  public void theUpdatedUserIsReturnedFromTheUpdateUserEndpoint() {
    User returnedUpdatedUser = response.body(User.class);

    Assertions.assertNotNull(returnedUpdatedUser);

    UserTestUtil.assertExpectedAgainstActual(updatedUser, returnedUpdatedUser);
  }

  @And("the updated user is correct in the database")
  public void theUpdatedUserIsCorrectInTheDatabase() {
    response = ApiCallTestUtil.getThroughApi(restClient, userByIdUrl);

    User retrievedUpdatedUser = response.body(User.class);

    Assertions.assertNotNull(retrievedUpdatedUser);

    UserTestUtil.assertExpectedAgainstActual(updatedUser, retrievedUpdatedUser);
  }
}