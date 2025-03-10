package com.ebsolutions.papertrail.financialdataproviderservice.household;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@SuppressWarnings("checkstyle:LineLength")
@RequiredArgsConstructor
public class HouseholdCreateAllSteps extends BaseTest {
  protected final HouseholdRepository householdRepository;

  private String requestContent;
  private MvcResult result;
  private Household expectedHouseholdOne;
  private Household expectedHouseholdTwo;

  @And("two valid households are part of the request body for the create all households endpoint")
  public void twoValidHouseholdsArePartOfTheRequestBodyForTheCreateAllHouseholdsEndpoint()
      throws JsonProcessingException {
    Household inputHouseholdOne = Household.builder()
        .name("first_household")
        .build();

    Household inputHouseholdTwo = Household.builder()
        .name("second_household")
        .build();

    expectedHouseholdOne =
        Household.builder()
            .householdId(1)
            .name("first_household")
            .build();

    expectedHouseholdTwo = Household.builder()
        .householdId(2)
        .name("second_household")
        .build();

    requestContent =
        objectMapper.writeValueAsString(Arrays.asList(inputHouseholdOne, inputHouseholdTwo));

    when(householdRepository.saveAll(any())).thenReturn(
        Arrays.asList(expectedHouseholdOne, expectedHouseholdTwo));
  }

  @And("the household in the request body has an invalid input")
  public void theHouseholdInTheRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int householdId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    Household inputHouseholdOne = Household.builder()
        .householdId(householdId)
        .name(HouseholdTestUtil.isEmptyString(dataTable.column(1).getFirst()))
        .build();

    requestContent =
        objectMapper.writeValueAsString(Collections.singletonList(inputHouseholdOne));
  }

  @And("no households are part of the request body")
  public void noHouseholdsArePartOfTheRequestBody() {
    requestContent = new ArrayList<>().toString();
  }

  @And("the connection to the database fails for the create all households endpoint")
  public void theConnectionToTheDatabaseFailsForTheCreateAllHouseholdsEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(householdRepository.saveAll(any())).thenThrow(dataProcessingException);
  }

  @When("the create all households endpoint is invoked")
  public void theCreateAllHouseholdsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.HOUSEHOLDS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the newly created households are returned from the create all households endpoint")
  public void theCorrectHouseholdsAreReturnedFromTheCreateAllHouseholdEndpoint()
      throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<Household> households = objectMapper.readerForListOf(Household.class).readValue(content);

    Household householdOne = households.getFirst();
    HouseholdTestUtil.assertExpectedHouseholdAgainstActualHousehold(expectedHouseholdOne,
        householdOne);

    Household householdTwo = households.getLast();
    HouseholdTestUtil.assertExpectedHouseholdAgainstActualHousehold(expectedHouseholdTwo,
        householdTwo);
  }

  @Then("the correct failure response is returned from the create all households endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheCreateAllHouseholdsEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while saving all households",
        errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the create all households endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheCreateAllHouseholdsEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Households cannot be empty",
        errorResponse.getMessages().getFirst());

    Mockito.verifyNoInteractions(householdRepository);
  }

  @Then("the correct failure response and message is returned from the create all households endpoint")
  public void theCorrectFailureResponseAndMessageIsReturnedFromTheCreateAllHouseholdsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.row(0).getFirst(), errorResponse.getMessages().getFirst());
  }
}
