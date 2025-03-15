package com.ebsolutions.papertrail.financialdataproviderservice.institution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class InstitutionUpdateSteps extends BaseTest {
  protected final InstitutionRepository institutionRepository;

  private String requestContent;
  private MvcResult result;
  private Institution expectedInstitutionOne;

  @And("the institution is part of the request body for the update institution endpoint")
  public void theInstitutionIsPartOfTheRequestBodyForTheUpdateInstitutionEndpoint()
      throws JsonProcessingException {
    int validInstitutionId = 1;

    Institution inputInstitutionOne = Institution.builder()
        .institutionId(validInstitutionId)
        .name("first_institution")
        .build();

    expectedInstitutionOne =
        Institution.builder()
            .institutionId(validInstitutionId)
            .name("first_institution")
            .build();

    requestContent =
        objectMapper.writeValueAsString(inputInstitutionOne);

    when(institutionRepository.findById(1L)).thenReturn(
        Optional.ofNullable(expectedInstitutionOne));

    when(institutionRepository.save(any())).thenReturn(expectedInstitutionOne);
  }

  @And("the institution id does not exist in the database")
  public void theInstitutionIdDoesNotExistInTheDatabase() {
    when(institutionRepository.findById(1L)).thenReturn(Optional.empty());
  }

  @And("the connection to the database fails for the update institution endpoint")
  public void theConnectionToTheDatabaseFailsForTheUpdateInstitutionEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(institutionRepository.save(any())).thenThrow(dataProcessingException);
  }

  @And("the institution in the update institution request body has an invalid input")
  public void theInstitutionInTheUpdateInstitutionRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int institutionId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    Institution inputInstitutionOne = Institution.builder()
        .institutionId(institutionId)
        .name(CommonTestUtil.isEmptyString(dataTable.column(1).getFirst()))
        .build();

    requestContent =
        objectMapper.writeValueAsString(inputInstitutionOne);
  }

  @When("the update institution endpoint is invoked")
  public void theUpdateInstitutionEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(put(Constants.INSTITUTIONS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the updated institution is returned from the update institution endpoint")
  public void theUpdatedInstitutionIsReturnedFromTheUpdateInstitutionEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    Institution institution = objectMapper.readValue(content, Institution.class);

    InstitutionTestUtil.assertExpectedAgainstActual(expectedInstitutionOne,
        institution);
  }

  @Then("the correct failure response is returned from the update institution endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheUpdateInstitutionEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while saving the institution",
        errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the update institution endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheUpdateInstitutionEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Institution Id does not exist: 1",
        errorResponse.getMessages().getFirst());
  }


  @Then("the correct failure response and message is returned from the update institution endpoint")
  public void theCorrectFailureResponseAndMessageIsReturnedFromTheUpdateInstitutionEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();


    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.row(0).getFirst(), errorResponse.getMessages().getFirst());
  }
}
