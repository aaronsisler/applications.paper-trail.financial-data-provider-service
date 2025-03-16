package com.ebsolutions.papertrail.financialdataproviderservice.institution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

@RequiredArgsConstructor
public class InstitutionCreateAllSteps extends BaseTest {
  protected final InstitutionRepository institutionRepository;

  private String requestContent;
  private MvcResult result;
  private Institution expectedInstitutionOne;
  private Institution expectedInstitutionTwo;

  @And("two valid institutions are part of the request body for the create all institutions endpoint")
  public void twoValidInstitutionsArePartOfTheRequestBodyForTheCreateAllInstitutionsEndpoint()
      throws JsonProcessingException {
    Institution inputInstitutionOne = Institution.builder()
        .name("first_institution")
        .build();

    Institution inputInstitutionTwo = Institution.builder()
        .name("second_institution")
        .build();

    expectedInstitutionOne =
        Institution.builder()
            .id(1)
            .name("first_institution")
            .build();

    expectedInstitutionTwo = Institution.builder()
        .id(2)
        .name("second_institution")
        .build();

    requestContent =
        objectMapper.writeValueAsString(Arrays.asList(inputInstitutionOne, inputInstitutionTwo));

    when(institutionRepository.saveAll(any())).thenReturn(
        Arrays.asList(expectedInstitutionOne, expectedInstitutionTwo));
  }

  @And("the institution in the request body has an invalid input")
  public void theInstitutionInTheRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int institutionId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    Institution inputInstitutionOne = Institution.builder()
        .id(institutionId)
        .name(CommonTestUtil.isEmptyString(dataTable.column(1).getFirst()))
        .build();

    requestContent =
        objectMapper.writeValueAsString(Collections.singletonList(inputInstitutionOne));
  }

  @And("no institutions are part of the request body")
  public void noInstitutionsArePartOfTheRequestBody() {
    requestContent = new ArrayList<>().toString();
  }

  @And("the connection to the database fails for the create all institutions endpoint")
  public void theConnectionToTheDatabaseFailsForTheCreateAllInstitutionsEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(institutionRepository.saveAll(any())).thenThrow(dataProcessingException);
  }

  @When("the create all institutions endpoint is invoked")
  public void theCreateAllInstitutionsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.INSTITUTIONS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the newly created institutions are returned from the create all institutions endpoint")
  public void theCorrectInstitutionsAreReturnedFromTheCreateAllInstitutionEndpoint()
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

  @Then("the correct failure response is returned from the create all institutions endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheCreateAllInstitutionsEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while saving all institutions",
        errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the create all institutions endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheCreateAllInstitutionsEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Institutions cannot be empty",
        errorResponse.getMessages().getFirst());

    Mockito.verifyNoInteractions(institutionRepository);
  }

  @Then("the correct failure response and message is returned from the create all institutions endpoint")
  public void theCorrectFailureResponseAndMessageIsReturnedFromTheCreateAllInstitutionsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.row(0).getFirst(), errorResponse.getMessages().getFirst());
  }
}
