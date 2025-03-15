package com.ebsolutions.papertrail.financialdataproviderservice.institution;

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
public class InstitutionDeleteSteps extends BaseTest {
  protected final InstitutionRepository institutionRepository;
  private final Integer validInstitutionId = 1;
  private String institutionUrl;
  private MvcResult result;

  @And("the institution id provided exists in the database")
  public void theInstitutionIdProvidedExistsInTheDatabase() {
    // Nothing to do here
  }

  @And("the institution id provided does not exist in the database")
  public void theInstitutionIdProvidedDoesNotExistInTheDatabase() {
    // Nothing to do here
  }

  @And("the institution id provided in the url is the correct format")
  public void theInstitutionIdProvidedInTheUrlIsTheCorrectFormat() {
    institutionUrl = Constants.INSTITUTIONS_URI + "/" + validInstitutionId;
  }

  @And("the institution id provided in the url is the incorrect format")
  public void theInstitutionIdProvidedInTheUrlIsTheIncorrectFormat() {
    String invalidInstitutionId = "abc";
    institutionUrl = Constants.INSTITUTIONS_URI + "/" + invalidInstitutionId;
  }

  @And("the connection to the database fails for the delete institution endpoint")
  public void theConnectionToTheDatabaseFailsForTheDeleteInstitutionEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    doThrow(dataProcessingException).when(institutionRepository).deleteById(any());
  }

  @When("the delete institution endpoint is invoked")
  public void theDeleteInstitutionEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(delete(institutionUrl))
        .andReturn();
  }

  @Then("the correct response is returned from the delete institution endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteInstitutionEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @And("the correct institution is deleted")
  public void theCorrectInstitutionIsDeleted() {
    Mockito.verify(institutionRepository).deleteById(validInstitutionId.longValue());
  }

  @Then("the correct failure response is returned from the delete institution endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheDeleteInstitutionEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }
}
