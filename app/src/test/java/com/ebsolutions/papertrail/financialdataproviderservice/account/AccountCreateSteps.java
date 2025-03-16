package com.ebsolutions.papertrail.financialdataproviderservice.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.household.Household;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMember;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMemberRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdMemberTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountCreateSteps extends BaseTest {
  private final HouseholdMemberRepository householdMemberRepository;
  private final UserRepository userRepository;
  private final HouseholdRepository householdRepository;


  private String requestContent;
  private MvcResult result;
  private HouseholdMember expectedHouseholdMember;

  @And("a valid account is part of the request body for the create account endpoint")
  public void aValidHouseholdMemberIsPartOfTheRequestBodyForTheCreateHouseholdMemberEndpoint()
      throws JsonProcessingException {
    HouseholdMember inputHouseholdMember = HouseholdMember
        .builder()
        .householdId(123)
        .userId(456)
        .build();

    expectedHouseholdMember =
        HouseholdMember.builder()
            .householdId(123)
            .userId(456)
            .build();

    requestContent = objectMapper.writeValueAsString(inputHouseholdMember);
  }

  @And("institution id exists in the database for the account")
  public void userIdExistsInTheDatabaseForTheHouseholdMember() {
    when(userRepository.findById(any())).thenReturn(Optional.of(User.builder().build()));
  }

  @And("household member id exists in the database for the account")
  public void householdIdExistsInTheDatabaseForTheHouseholdMember() {
    when(householdRepository.findById(any())).thenReturn(Optional.of(Household.builder().build()));
  }

  @And("the database connection succeeds for create account")
  public void theDatabaseConnectionSucceedsForCreateHouseholdMember() {
    when(householdMemberRepository.save(any())).thenReturn(expectedHouseholdMember);
  }

  @And("household member id does not exist in the account")
  public void householdIdDoesNotExistInTheHouseholdMember() {
    when(householdRepository.findById(any())).thenReturn(Optional.empty());
  }

  @And("institution id does not exist in the account")
  public void userIdDoesNotExistInTheHouseholdMember() {
    when(userRepository.findById(any())).thenReturn(Optional.empty());
  }

  @And("the account in the request body has an invalid input")
  public void theHouseholdMemberInTheRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int householdMemberId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    int householdId = dataTable.column(1).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(1).getFirst());

    int userId = dataTable.column(2).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(2).getFirst());

    HouseholdMember inputHouseholdMember = HouseholdMember.builder()
        .id(householdMemberId)
        .householdId(householdId)
        .userId(userId)
        .build();

    requestContent =
        objectMapper.writeValueAsString(inputHouseholdMember);
  }

  @And("the database save fails given a user or household was deleted during the create account database call")
  public void theDatabaseSaveFailsGivenAUserOrHouseholdWasDeletedDuringTheCreateHouseholdMemberDatabaseCall() {
    doThrow(DataIntegrityViolationException.class)
        .when(householdMemberRepository).save(any());
  }

  @When("the create account endpoint is invoked")
  public void theCreateHouseholdMemberEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.HOUSEHOLD_MEMBERS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the newly created account is returned from the create account endpoint")
  public void theNewlyCreatedHouseholdMemberIsReturnedFromTheCreateHouseholdMemberEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    HouseholdMember householdMember = objectMapper.readValue(content, HouseholdMember.class);

    HouseholdMemberTestUtil.assertExpectedAgainstActual(expectedHouseholdMember, householdMember);
  }

  @Then("the correct bad request response is returned from the create account endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheCreateHouseholdMemberEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }

  @And("the connection to the database fails for the create account endpoint")
  public void theConnectionToTheDatabaseFailsForTheCreateHouseholdMemberEndpoint() {
    when(householdMemberRepository.save(any())).thenThrow(new DataProcessingException());
  }

  @Then("the account is not created")
  public void theHouseholdMemberIsNotCreated() {
    Mockito.verifyNoInteractions(householdMemberRepository);
  }
}
