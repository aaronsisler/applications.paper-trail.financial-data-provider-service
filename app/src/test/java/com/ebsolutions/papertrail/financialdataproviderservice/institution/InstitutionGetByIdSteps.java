package com.ebsolutions.papertrail.financialdataproviderservice.institution;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.InstitutionTestUtil;
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
public class InstitutionGetByIdSteps extends BaseTest {
  protected final InstitutionRepository institutionRepository;
  private final int institutionId = 1;
  private MvcResult result;
  private Institution expectedInstitution;
  private String getInstitutionByIdUrl;


  @And("the requested institution exist in the database")
  public void theRequestedInstitutionExistInTheDatabase() {
    expectedInstitution =
        Institution.builder()
            .id(institutionId)
            .name("first_institution")
            .build();

    when(institutionRepository.findById((long) institutionId)).thenReturn(
        Optional.ofNullable(expectedInstitution));
  }

  @And("no institution for the given id exists in the database")
  public void noInstitutionForTheGivenIdExistsInTheDatabase() {
    // Nothing to do here
  }

  @And("the institution id provided in the url is the correct format for the get institution by id endpoint")
  public void theInstitutionIdProvidedInTheUrlIsTheCorrectFormatForTheGetInstitutionByIdEndpoint() {
    getInstitutionByIdUrl = Constants.INSTITUTIONS_URI + "/" + institutionId;
  }

  @And("the institution id provided in the url is the incorrect format for the get institution by id endpoint")
  public void theInstitutionIdProvidedInTheUrlIsTheIncorrectFormatForTheGetInstitutionByIdEndpoint() {
    String invalidInstitutionId = "abc";
    getInstitutionByIdUrl = Constants.INSTITUTIONS_URI + "/" + invalidInstitutionId;
  }


  @When("the get institution by id endpoint is invoked")
  public void theGetInstitutionByIdEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getInstitutionByIdUrl))
        .andReturn();
  }

  @Then("the correct empty response is returned from the get institution by id endpoint")
  public void theCorrectEmptyResponseIsReturnedFromTheGetInstitutionByIdEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct institution are returned from the get institution by id endpoint")
  public void theCorrectInstitutionAreReturnedFromTheGetInstitutionByIdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    Institution institution = objectMapper.readValue(content, Institution.class);

    InstitutionTestUtil.assertExpectedAgainstActual(expectedInstitution, institution);
  }

  @Then("the correct failure response is returned from the get institution by id endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetInstitutionByIdEndpoint(
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
