package com.ebsolutions.papertrail.financialdataproviderservice.household;

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
public class HouseholdDeleteByIdSteps extends BaseTest {
  protected final HouseholdRepository householdRepository;
  private final Integer validHouseholdId = 1;
  private String householdUrl;
  private MvcResult result;

  @And("the household id provided exists in the database")
  public void theHouseholdIdProvidedExistsInTheDatabase() {
    // Nothing to do here
  }

  @And("the household id provided does not exist in the database")
  public void theHouseholdIdProvidedDoesNotExistInTheDatabase() {
    // Nothing to do here
  }

  @And("the household id provided in the url is the correct format")
  public void theHouseholdIdProvidedInTheUrlIsTheCorrectFormat() {
    householdUrl = Constants.HOUSEHOLDS_URI + "/" + validHouseholdId;
  }

  @And("the household id provided in the url is the incorrect format")
  public void theHouseholdIdProvidedInTheUrlIsTheIncorrectFormat() {
    String invalidHouseholdId = "abc";
    householdUrl = Constants.HOUSEHOLDS_URI + "/" + invalidHouseholdId;
  }

  @And("the connection to the database fails for the delete household endpoint")
  public void theConnectionToTheDatabaseFailsForTheDeleteHouseholdEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    doThrow(dataProcessingException).when(householdRepository).deleteById(any());
  }

  @When("the delete household endpoint is invoked")
  public void theDeleteHouseholdEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(delete(householdUrl))
        .andReturn();
  }

  @Then("the correct response is returned from the delete household endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteHouseholdEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @And("the correct household is deleted")
  public void theCorrectHouseholdIsDeleted() {
    Mockito.verify(householdRepository).deleteById(validHouseholdId.longValue());
  }

  @Then("the correct failure response is returned from the delete household endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheDeleteHouseholdEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }
}
