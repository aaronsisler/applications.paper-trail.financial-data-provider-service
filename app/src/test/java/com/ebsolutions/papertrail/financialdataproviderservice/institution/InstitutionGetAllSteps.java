package com.ebsolutions.papertrail.financialdataproviderservice.institution;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.InstitutionTestUtil;
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
public class InstitutionGetAllSteps extends BaseTest {
  protected final InstitutionRepository institutionRepository;

  private MvcResult result;
  private Institution expectedInstitutionOne;
  private Institution expectedInstitutionTwo;

  @And("two institutions exist in the database")
  public void twoInstitutionsExistInTheDatabase() {
    expectedInstitutionOne =
        Institution.builder()
            .institutionId(1)
            .name("first_institution")
            .build();

    expectedInstitutionTwo =
        Institution.builder()
            .institutionId(2)
            .name("second_institution")
            .build();

    when(institutionRepository.findAll()).thenReturn(
        Arrays.asList(expectedInstitutionOne, expectedInstitutionTwo));
  }

  @And("no institutions exist")
  public void noInstitutionsExist() {
    when(institutionRepository.findAll()).thenReturn(Collections.emptyList());
  }

  @And("the connection to the database fails for the get all institutions endpoint")
  public void theConnectionToTheDatabaseFailsForTheGetAllInstitutionsEndpoint() {
    when(institutionRepository.findAll()).thenThrow(new DataProcessingException());
  }

  @When("the get all institutions endpoint is invoked")
  public void theGetAllInstitutionsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(get(Constants.INSTITUTIONS_URI)).andReturn();
  }

  @Then("the correct institutions are returned")
  public void theCorrectInstitutionsAreReturned()
      throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<Institution> institutions =
        objectMapper.readerForListOf(Institution.class).readValue(content);

    Institution institutionOne = institutions.getFirst();
    InstitutionTestUtil.assertExpectedAgainstActual(expectedInstitutionOne,
        institutionOne);

    Institution institutionTwo = institutions.getLast();
    InstitutionTestUtil.assertExpectedAgainstActual(expectedInstitutionTwo,
        institutionTwo);
  }

  @Then("the correct empty institutions response is returned")
  public void theCorrectEmptyInstitutionsResponseIsReturned() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct failure response is returned from the get all institutions endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAllInstitutionsEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while fetching all institutions",
        errorResponse.getMessages().getFirst());
  }
}
