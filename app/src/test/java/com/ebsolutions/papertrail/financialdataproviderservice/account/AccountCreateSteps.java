package com.ebsolutions.papertrail.financialdataproviderservice.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMember;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMemberRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.institution.Institution;
import com.ebsolutions.papertrail.financialdataproviderservice.institution.InstitutionRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.AccountTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
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
  private final InstitutionRepository institutionRepository;
  private final AccountRepository accountRepository;


  private String requestContent;
  private MvcResult result;
  private Account expectedAccount;

  @And("a valid account is part of the request body for the create account endpoint")
  public void aValidAccountIsPartOfTheRequestBodyForTheCreateAccountEndpoint()
      throws JsonProcessingException {
    Account inputAccount = Account
        .builder()
        .householdMemberId(123)
        .institutionId(456)
        .name("Input Account Name")
        .nickname("Input Account Nickname")
        .build();

    expectedAccount =
        Account.builder()
            .householdMemberId(123)
            .institutionId(456)
            .name("Input Account Name")
            .nickname("Input Account Nickname")
            .build();

    requestContent = objectMapper.writeValueAsString(inputAccount);
  }

  @And("institution id exists in the database for the account")
  public void institutionIdExistsInTheDatabaseForTheAccount() {
    when(institutionRepository.findById(anyLong()))
        .thenReturn(Optional.of(Institution.builder().build()));
  }

  @And("household member id exists in the database for the account")
  public void householdMemberIdExistsInTheDatabaseForTheAccount() {
    when(householdMemberRepository.findById(any())).thenReturn(
        Optional.of(HouseholdMember.builder().build()));
  }

  @And("the database connection succeeds for create account")
  public void theDatabaseConnectionSucceedsForCreateAccount() {
    when(accountRepository.save(any())).thenReturn(expectedAccount);
  }

  @And("household member id does not exist in the account")
  public void householdMemberIdDoesNotExistInTheAccount() {
    when(householdMemberRepository.findById(any())).thenReturn(Optional.empty());
  }

  @And("institution id does not exist in the account")
  public void institutionIdDoesNotExistInTheAccount() {
    when(institutionRepository.findById(any())).thenReturn(Optional.empty());
  }

  @And("the connection to the database fails for the create account endpoint")
  public void theConnectionToTheDatabaseFailsForTheCreateAccountEndpoint() {
    when(accountRepository.save(any())).thenThrow(new DataProcessingException());
  }

  @And("the connection to the database fails for the get household member by id")
  public void theConnectionToTheDatabaseFailsForTheGetHouseholdMemberById() {
    when(householdMemberRepository.findById(any()))
        .thenThrow(new DataProcessingException());
  }

  @And("the database save fails given a institution or household member was deleted during the create account database call")
  public void theDatabaseSaveFailsGivenAInstitutionOrHouseholdMemberWasDeletedDuringTheCreateAccountDatabaseCall() {
    doThrow(DataIntegrityViolationException.class).when(accountRepository).save(any());
  }

  @And("the account in the request body has an invalid input")
  public void theAccountInTheRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int accountId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    int householdMemberId = dataTable.column(1).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(1).getFirst());

    int institutionId = dataTable.column(2).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(2).getFirst());

    Account inputAccount = Account.builder()
        .id(accountId)
        .householdMemberId(householdMemberId)
        .institutionId(institutionId)
        .name(CommonTestUtil.isEmptyString(dataTable.column(3).getFirst()))
        .nickname(CommonTestUtil.isEmptyString(dataTable.column(4).getFirst()))
        .build();

    requestContent =
        objectMapper.writeValueAsString(inputAccount);
  }

  @When("the create account endpoint is invoked")
  public void theCreateAccountEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.ACCOUNTS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the newly created account is returned from the create account endpoint")
  public void theNewlyCreatedAccountIsReturnedFromTheCreateAccountEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    Account account = objectMapper.readValue(content, Account.class);

    AccountTestUtil.assertExpectedAgainstActual(expectedAccount, account);
  }

  @Then("the correct bad request response is returned from the create account endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheCreateAccountEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }

  @And("the account is not created")
  public void theAccountIsNotCreated() {
    Mockito.verifyNoInteractions(accountRepository);
  }
}
