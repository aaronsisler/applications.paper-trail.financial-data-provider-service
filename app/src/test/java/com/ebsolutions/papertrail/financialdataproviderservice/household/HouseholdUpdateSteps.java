package com.ebsolutions.papertrail.financialdataproviderservice.household;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class HouseholdUpdateSteps extends BaseTest {
  protected final HouseholdRepository householdRepository;

  private String requestContent;
  private MvcResult result;
  private Household expectedHouseholdOne;

  @And("the household is part of the request body for the update household endpoint")
  public void theHouseholdIsPartOfTheRequestBodyForTheUpdateHouseholdEndpoint()
      throws JsonProcessingException {
    int validHouseholdId = 1;

    Household inputHouseholdOne = Household.builder()
        .id(validHouseholdId)
        .name("first_household")
        .build();

    expectedHouseholdOne =
        Household.builder()
            .id(validHouseholdId)
            .name("first_household")
            .build();

    requestContent =
        objectMapper.writeValueAsString(inputHouseholdOne);

    when(householdRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedHouseholdOne));

    when(householdRepository.save(any())).thenReturn(expectedHouseholdOne);
  }

  @And("the household id does not exist in the database")
  public void theHouseholdIdDoesNotExistInTheDatabase() {
    when(householdRepository.findById(1L)).thenReturn(Optional.empty());
  }

  @And("the connection to the database fails for the update household endpoint")
  public void theConnectionToTheDatabaseFailsForTheUpdateHouseholdEndpoint() {
    when(householdRepository.save(any())).thenThrow(new DataProcessingException());
  }

  @And("the household in the update household request body has an invalid input")
  public void theHouseholdInTheUpdateHouseholdRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int householdId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    Household inputHouseholdOne = Household.builder()
        .id(householdId)
        .name(CommonTestUtil.isEmptyString(dataTable.column(1).getFirst()))
        .build();

    requestContent =
        objectMapper.writeValueAsString(inputHouseholdOne);
  }

  @When("the update household endpoint is invoked")
  public void theUpdateHouseholdEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(put(Constants.HOUSEHOLDS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the updated household is returned from the update household endpoint")
  public void theUpdatedHouseholdIsReturnedFromTheUpdateHouseholdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    Household household = objectMapper.readValue(content, Household.class);

    HouseholdTestUtil.assertExpectedAgainstActual(expectedHouseholdOne,
        household);
  }

  @Then("the correct failure response is returned from the update household endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheUpdateHouseholdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while saving the household",
        errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the update household endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheUpdateHouseholdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Household Id does not exist: 1",
        errorResponse.getMessages().getFirst());
  }


  @Then("the correct failure response and message is returned from the update household endpoint")
  public void theCorrectFailureResponseAndMessageIsReturnedFromTheUpdateHouseholdEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();


    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.row(0).getFirst(), errorResponse.getMessages().getFirst());
  }
}
