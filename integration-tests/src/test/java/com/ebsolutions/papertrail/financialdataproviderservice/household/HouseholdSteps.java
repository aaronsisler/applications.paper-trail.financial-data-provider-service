package com.ebsolutions.papertrail.financialdataproviderservice.household;

import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.model.Household;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdTestUtil;
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
public class HouseholdSteps extends BaseStep {
  private final List<Integer> newlyCreateHouseholdIds = new ArrayList<>();

  private String requestContent;
  private RestClient.ResponseSpec response;
  private Household expectedHouseholdOne;
  private Household expectedHouseholdTwo;
  private int resultHouseholdId;
  private String householdByIdUrl;
  private Household updatedHousehold;

  @Before
  public void setup() {
    expectedHouseholdOne =
        Household.builder()
            .name("first_household")
            .build();

    expectedHouseholdTwo = Household.builder()
        .name("second_household")
        .build();
  }

  @And("two valid households are part of the request body for the create all households endpoint")
  public void twoValidHouseholdsArePartOfTheRequestBodyForTheCreateAllHouseholdsEndpoint()
      throws JsonProcessingException {
    requestContent =
        objectMapper.writeValueAsString(Arrays.asList(expectedHouseholdOne, expectedHouseholdTwo));
  }

  @When("the create all households endpoint is invoked")
  public void theCreateAllHouseholdsEndpointIsInvoked() {
    response = createHouseholdThroughApi();
  }

  @Then("the newly created households are returned from the create all households endpoint")
  public void theNewlyCreatedHouseholdsAreReturnedFromTheCreateAllHouseholdsEndpoint() {
    List<Household> households = response.body(
        new ParameterizedTypeReference<ArrayList<Household>>() {
        });

    Assertions.assertNotNull(households);
    Assertions.assertEquals(2, households.size());

    Household householdOne = households.getFirst();
    HouseholdTestUtil.assertExpectedAgainstCreated(expectedHouseholdOne,
        householdOne);
    newlyCreateHouseholdIds.add(householdOne.getHouseholdId());

    Household householdTwo = households.getLast();
    HouseholdTestUtil.assertExpectedAgainstCreated(expectedHouseholdTwo,
        householdTwo);
    newlyCreateHouseholdIds.add(householdTwo.getHouseholdId());
  }

  @When("the get all households endpoint is invoked")
  public void theGetAllHouseholdsEndpointIsInvoked() {
    response = restClient
        .get()
        .uri(TestConstants.HOUSEHOLDS_URI)
        .retrieve();
  }

  @Then("the correct households are returned from the get all households endpoint")
  public void theCorrectHouseholdsAreReturnedFromTheGetAllHouseholdsEndpoint() {
    List<Household> households = response.body(
        new ParameterizedTypeReference<ArrayList<Household>>() {
        });

    Assertions.assertNotNull(households);
    List<Household> createdHouseholds =
        households.stream()
            .filter(household -> newlyCreateHouseholdIds.contains(household.getHouseholdId()))
            .toList();
    Assertions.assertEquals(2, createdHouseholds.size());

    Household householdOne = createdHouseholds.getFirst();
    HouseholdTestUtil.assertExpectedAgainstCreated(expectedHouseholdOne,
        householdOne);

    Household householdTwo = createdHouseholds.getLast();
    HouseholdTestUtil.assertExpectedAgainstCreated(expectedHouseholdTwo,
        householdTwo);
  }

  @And("the household id provided exists in the database")
  public void theHouseholdIdProvidedExistsInTheDatabase(DataTable dataTable) throws Exception {
    Household inputHousehold = Household.builder()
        .name(CommonTestUtil.isEmptyString(dataTable.column(0).getFirst()))
        .build();

    requestContent =
        objectMapper.writeValueAsString(Collections.singletonList(inputHousehold));

    response = createHouseholdThroughApi();

    List<Household> households = response.body(
        new ParameterizedTypeReference<ArrayList<Household>>() {
        });

    Assertions.assertNotNull(households);
    Assertions.assertEquals(1, households.size());

    Household createdHousehold = households.getFirst();
    HouseholdTestUtil.assertExpectedAgainstCreated(inputHousehold,
        createdHousehold);

    Assertions.assertNotNull(createdHousehold);
    Assertions.assertNotNull(createdHousehold.getHouseholdId());

    resultHouseholdId = createdHousehold.getHouseholdId();
    householdByIdUrl = TestConstants.HOUSEHOLDS_URI + "/" + resultHouseholdId;

    response = getHouseholdThroughApi();

    Household retrievedCreatedHousehold = response.body(Household.class);

    Assertions.assertNotNull(retrievedCreatedHousehold);

    HouseholdTestUtil.assertExpectedAgainstActual(createdHousehold,
        retrievedCreatedHousehold);
  }

  @And("an update for the household is valid and part of the request body for the update household endpoint")
  public void anUpdateForTheHouseholdIsValidAndPartOfTheRequestBodyForTheUpdateHouseholdEndpoint()
      throws JsonProcessingException {
    updatedHousehold = Household.builder()
        .householdId(resultHouseholdId)
        .name("updated_name")
        .build();

    requestContent =
        objectMapper.writeValueAsString(updatedHousehold);
  }

  @When("the update household endpoint is invoked")
  public void theUpdateHouseholdEndpointIsInvoked() {
    response = updateHouseholdThroughApi();
  }

  @When("the delete household endpoint is invoked")
  public void theDeleteHouseholdEndpointIsInvoked() throws Exception {
    response = deleteHouseholdThroughApi();
  }

  @Then("the correct response is returned from the delete household endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteHouseholdEndpoint() {
    checkForNoContentStatusCode(response);
  }

  @And("the correct household is deleted")
  public void theCorrectHouseholdIsDeleted() throws Exception {
    response = getHouseholdThroughApi();

    checkForNoContentStatusCode(response);
  }

  @Then("the updated household is returned from the update household endpoint")
  public void theUpdatedHouseholdIsReturnedFromTheUpdateHouseholdEndpoint() {
    Household returnedUpdatedHousehold = response.body(Household.class);

    Assertions.assertNotNull(returnedUpdatedHousehold);

    HouseholdTestUtil.assertExpectedAgainstActual(updatedHousehold,
        returnedUpdatedHousehold);
  }

  @And("the updated household is correct in the database")
  public void theUpdatedHouseholdIsCorrectInTheDatabase() {
    Household retrievedUpdatedHousehold = response.body(Household.class);

    Assertions.assertNotNull(retrievedUpdatedHousehold);

    HouseholdTestUtil.assertExpectedAgainstActual(updatedHousehold,
        retrievedUpdatedHousehold);
  }

  private RestClient.ResponseSpec createHouseholdThroughApi() {
    return checkForErrorStatusCodes(restClient
        .post()
        .uri(TestConstants.HOUSEHOLDS_URI)
        .accept(MediaType.APPLICATION_JSON)
        .body(requestContent)
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve());
  }

  private RestClient.ResponseSpec updateHouseholdThroughApi() {
    return checkForErrorStatusCodes(restClient
        .put()
        .uri(TestConstants.HOUSEHOLDS_URI)
        .accept(MediaType.APPLICATION_JSON)
        .body(requestContent)
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve());
  }

  private RestClient.ResponseSpec getHouseholdThroughApi() {
    return checkForErrorStatusCodes(restClient
        .get()
        .uri(householdByIdUrl)
        .retrieve());
  }

  private RestClient.ResponseSpec deleteHouseholdThroughApi() {
    return checkForErrorStatusCodes(restClient
        .delete()
        .uri(householdByIdUrl)
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