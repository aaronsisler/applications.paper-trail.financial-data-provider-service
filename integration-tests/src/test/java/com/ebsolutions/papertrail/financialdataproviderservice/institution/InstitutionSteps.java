package com.ebsolutions.papertrail.financialdataproviderservice.institution;


import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.model.Institution;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.util.ApiCallTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.InstitutionTestUtil;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Slf4j
public class InstitutionSteps extends BaseStep {
  private final List<Integer> newlyCreateInstitutionIds = new ArrayList<>();

  private String requestContent;
  private RestClient.ResponseSpec response;
  private Institution expectedInstitutionOne;
  private Institution expectedInstitutionTwo;
  private int resultInstitutionId;
  private String institutionByIdUrl;
  private Institution updatedInstitution;

  @Before
  public void setup() {
    expectedInstitutionOne =
        Institution.builder()
            .name("first_institution")
            .build();

    expectedInstitutionTwo = Institution.builder()
        .name("second_institution")
        .build();
  }

  @And("two valid institutions are part of the request body for the create all institutions endpoint")
  public void twoValidInstitutionsArePartOfTheRequestBodyForTheCreateAllInstitutionsEndpoint()
      throws JsonProcessingException {
    requestContent =
        objectMapper.writeValueAsString(
            Arrays.asList(expectedInstitutionOne, expectedInstitutionTwo));
  }

  @When("the create all institutions endpoint is invoked")
  public void theCreateAllInstitutionsEndpointIsInvoked() {
    response =
        ApiCallTestUtil.createThroughApi(restClient, TestConstants.INSTITUTIONS_URI,
            requestContent);
  }

  @Then("the newly created institutions are returned from the create all institutions endpoint")
  public void theNewlyCreatedInstitutionsAreReturnedFromTheCreateAllInstitutionsEndpoint() {
    System.out.println(response);
    List<Institution> institutions = response.body(
        new ParameterizedTypeReference<ArrayList<Institution>>() {
        });

    Assertions.assertNotNull(institutions);
    Assertions.assertEquals(2, institutions.size());

    Institution institutionOne = institutions.getFirst();
    InstitutionTestUtil.assertExpectedAgainstCreated(expectedInstitutionOne,
        institutionOne);
    newlyCreateInstitutionIds.add(institutionOne.getInstitutionId());

    Institution institutionTwo = institutions.getLast();
    InstitutionTestUtil.assertExpectedAgainstCreated(expectedInstitutionTwo,
        institutionTwo);
    newlyCreateInstitutionIds.add(institutionTwo.getInstitutionId());
  }

  @When("the get all institutions endpoint is invoked")
  public void theGetAllInstitutionsEndpointIsInvoked() {
    response = restClient
        .get()
        .uri(TestConstants.INSTITUTIONS_URI)
        .retrieve();
  }

  @Then("the correct institutions are returned from the get all institutions endpoint")
  public void theCorrectInstitutionsAreReturnedFromTheGetAllInstitutionsEndpoint() {
    List<Institution> institutions = response.body(
        new ParameterizedTypeReference<ArrayList<Institution>>() {
        });

    Assertions.assertNotNull(institutions);
    List<Institution> createdInstitutions =
        institutions.stream()
            .filter(
                institution -> newlyCreateInstitutionIds.contains(institution.getInstitutionId()))
            .toList();
    Assertions.assertEquals(2, createdInstitutions.size());

    Institution institutionOne = createdInstitutions.getFirst();
    InstitutionTestUtil.assertExpectedAgainstCreated(expectedInstitutionOne,
        institutionOne);

    Institution institutionTwo = createdInstitutions.getLast();
    InstitutionTestUtil.assertExpectedAgainstCreated(expectedInstitutionTwo,
        institutionTwo);
  }

  @And("the institution id provided exists in the database")
  public void theInstitutionIdProvidedExistsInTheDatabase() {
    Institution databaseSetupInstitution = InstitutionTestUtil.getTestDataInstitution();

    Assertions.assertNotNull(databaseSetupInstitution);
    if (databaseSetupInstitution.getInstitutionId() == null) {
      Assertions.fail("Data setup failed for institution");
    }
    resultInstitutionId = databaseSetupInstitution.getInstitutionId();
    institutionByIdUrl = TestConstants.INSTITUTIONS_URI + "/" + resultInstitutionId;

    response = getInstitutionThroughApi();

    Institution retrievedCreatedInstitution = response.body(Institution.class);

    Assertions.assertNotNull(retrievedCreatedInstitution);

    InstitutionTestUtil.assertExpectedAgainstActual(databaseSetupInstitution,
        retrievedCreatedInstitution);
  }

  @And("an update for the institution is valid and part of the request body for the update institution endpoint")
  public void anUpdateForTheInstitutionIsValidAndPartOfTheRequestBodyForTheUpdateInstitutionEndpoint()
      throws JsonProcessingException {
    updatedInstitution = Institution.builder()
        .institutionId(resultInstitutionId)
        .name("updated_name")
        .build();

    requestContent =
        objectMapper.writeValueAsString(updatedInstitution);
  }

  @When("the update institution endpoint is invoked")
  public void theUpdateInstitutionEndpointIsInvoked() {
    response = ApiCallTestUtil.updateThroughApi(restClient, TestConstants.INSTITUTIONS_URI,
        requestContent);
  }

  @When("the delete institution endpoint is invoked")
  public void theDeleteInstitutionEndpointIsInvoked() throws Exception {
    response = deleteInstitutionThroughApi();
  }

  @Then("the correct response is returned from the delete institution endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteInstitutionEndpoint() {
    checkForNoContentStatusCode(response);
  }

  @And("the correct institution is deleted")
  public void theCorrectInstitutionIsDeleted() throws Exception {
    response = getInstitutionThroughApi();

    checkForNoContentStatusCode(response);
  }

  @Then("the updated institution is returned from the update institution endpoint")
  public void theUpdatedInstitutionIsReturnedFromTheUpdateInstitutionEndpoint() {
    Institution returnedUpdatedInstitution = response.body(Institution.class);

    Assertions.assertNotNull(returnedUpdatedInstitution);

    InstitutionTestUtil.assertExpectedAgainstActual(updatedInstitution,
        returnedUpdatedInstitution);
  }

  @And("the updated institution is correct in the database")
  public void theUpdatedInstitutionIsCorrectInTheDatabase() {
    Institution retrievedUpdatedInstitution = response.body(Institution.class);

    Assertions.assertNotNull(retrievedUpdatedInstitution);

    InstitutionTestUtil.assertExpectedAgainstActual(updatedInstitution,
        retrievedUpdatedInstitution);
  }

  private RestClient.ResponseSpec getInstitutionThroughApi() {
    return checkForErrorStatusCodes(restClient
        .get()
        .uri(institutionByIdUrl)
        .retrieve());
  }

  private RestClient.ResponseSpec deleteInstitutionThroughApi() {
    return checkForErrorStatusCodes(restClient
        .delete()
        .uri(institutionByIdUrl)
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