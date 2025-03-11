package com.ebsolutions.papertrail.financialdataproviderservice.household;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdTestUtil;
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
public class HouseholdGetByIdSteps extends BaseTest {
  protected final HouseholdRepository householdRepository;
  private final int householdId = 1;
  private MvcResult result;
  private Household expectedHousehold;
  private String getHouseholdByIdUrl;


  @And("the requested household exist in the database")
  public void theRequestedHouseholdExistInTheDatabase() {
    expectedHousehold =
        Household.builder()
            .householdId(householdId)
            .name("first_household")
            .build();

    when(householdRepository.findById((long) householdId)).thenReturn(
        Optional.ofNullable(expectedHousehold));
  }

  @And("no household for the given id exists in the database")
  public void noHouseholdForTheGivenIdExistsInTheDatabase() {
    // Nothing to do here
  }

  @And("the household id provided in the url is the correct format for the get household by id endpoint")
  public void theHouseholdIdProvidedInTheUrlIsTheCorrectFormatForTheGetHouseholdByIdEndpoint() {
    getHouseholdByIdUrl = Constants.HOUSEHOLDS_URI + "/" + householdId;
  }

  @And("the household id provided in the url is the incorrect format for the get household by id endpoint")
  public void theHouseholdIdProvidedInTheUrlIsTheIncorrectFormatForTheGetHouseholdByIdEndpoint() {
    String invalidHouseholdId = "abc";
    getHouseholdByIdUrl = Constants.HOUSEHOLDS_URI + "/" + invalidHouseholdId;
  }

  @And("the connection to the database fails for the get household by id endpoint")
  public void theConnectionToTheDatabaseFailsForTheGetHouseholdByIdEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    doThrow(dataProcessingException).when(householdRepository).findById(any());
  }

  @When("the get household by id endpoint is invoked")
  public void theGetHouseholdByIdEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getHouseholdByIdUrl))
        .andReturn();
  }

  @Then("the correct empty response is returned from the get household by id endpoint")
  public void theCorrectEmptyResponseIsReturnedFromTheGetHouseholdByIdEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct household are returned from the get household by id endpoint")
  public void theCorrectHouseholdAreReturnedFromTheGetHouseholdByIdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    Household household = objectMapper.readValue(content, Household.class);

    HouseholdTestUtil.assertExpectedAgainstActual(expectedHousehold, household);
  }

  @Then("the correct failure response is returned from the get household by id endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetHouseholdByIdEndpoint(
      DataTable dataTable)
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }


}
