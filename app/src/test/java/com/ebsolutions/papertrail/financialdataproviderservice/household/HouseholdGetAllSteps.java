package com.ebsolutions.papertrail.financialdataproviderservice.household;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class HouseholdGetAllSteps extends BaseTest {
  protected final HouseholdRepository householdRepository;

  private MvcResult result;
  private Household expectedHouseholdOne;
  private Household expectedHouseholdTwo;

  @And("two households exist in the database")
  public void twoHouseholdsExistInTheDatabase() {
    expectedHouseholdOne =
        Household.builder()
            .householdId(1)
            .name("first_household")
            .build();

    expectedHouseholdTwo =
        Household.builder()
            .householdId(2)
            .name("second_household")
            .build();

    when(householdRepository.findAll()).thenReturn(
        Arrays.asList(expectedHouseholdOne, expectedHouseholdTwo));
  }

  @And("no households exist")
  public void noHouseholdsExist() {
    when(householdRepository.findAll()).thenReturn(Collections.emptyList());
  }

  @And("the connection to the database fails for the get all households endpoint")
  public void theConnectionToTheDatabaseFailsForTheGetAllHouseholdsEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(householdRepository.findAll()).thenThrow(dataProcessingException);
  }

  @When("the get all households endpoint is invoked")
  public void theGetAllHouseholdsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(get(Constants.HOUSEHOLDS_URI)).andReturn();
  }

  @Then("the correct households are returned")
  public void theCorrectHouseholdsAreReturned()
      throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<Household> households = objectMapper.readerForListOf(Household.class).readValue(content);

    Household householdOne = households.getFirst();
    HouseholdTestUtil.assertExpectedAgainstActual(expectedHouseholdOne,
        householdOne);

    Household householdTwo = households.getLast();
    HouseholdTestUtil.assertExpectedAgainstActual(expectedHouseholdTwo,
        householdTwo);
  }

  @Then("the correct empty households response is returned")
  public void theCorrectEmptyHouseholdsResponseIsReturned() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct failure response is returned from the get all households endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAllHouseholdsEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while fetching all households",
        errorResponse.getMessages().getFirst());
  }
}
