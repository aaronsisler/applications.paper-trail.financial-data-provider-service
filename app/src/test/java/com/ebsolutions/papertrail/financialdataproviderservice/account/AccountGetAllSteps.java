package com.ebsolutions.papertrail.financialdataproviderservice.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.AccountTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountGetAllSteps extends BaseTest {
  private static final int HOUSEHOLD_MEMBER_ID_TO_BE_FOUND = 456;

  private final AccountRepository accountRepository;
  private final AccountRepository householdMemberRepository;

  private MvcResult result;
  private Account expectedAccountOne;
  private Account expectedAccountTwo;
  private Account expectedAccountThree;
  private String getAccountUrl;

  @BeforeEach
  public void setup() {
    expectedAccountOne = null;
    expectedAccountTwo = null;
    expectedAccountThree = null;
  }

  @And("two accounts exist in the database for a given household member id")
  public void twoAccountsExistInTheDatabaseForAGivenUserId() {
    expectedAccountOne =
        Account.builder()
            .id(1)
            .householdMemberId(HOUSEHOLD_MEMBER_ID_TO_BE_FOUND)
            .institutionId(123)
            .name("Name")
            .nickname("Nickname")
            .build();

    expectedAccountTwo =
        Account.builder()
            .id(2)
            .householdMemberId(HOUSEHOLD_MEMBER_ID_TO_BE_FOUND)
            .institutionId(456)
            .name("Name")
            .nickname("Nickname")
            .build();
  }

  @And("one account exists in the database for a different household member id")
  public void oneAccountExistsInTheDatabaseForADifferentUserId() {
    expectedAccountThree =
        Account.builder()
            .id(3)
            .householdMemberId(123)
            .institutionId(12345)
            .name("Name")
            .nickname("Nickname")
            .build();
  }

  @And("the url does contain the household member id query param for the get all accounts endpoint")
  public void theUrlDoesContainTheUserIdQueryParamForTheGetAllAccountsEndpoint() {
    getAccountUrl =
        Constants.ACCOUNTS_URI + "?householdMemberId=" + HOUSEHOLD_MEMBER_ID_TO_BE_FOUND;
  }

  @And("the database connection succeeds for get all accounts")
  public void theDatabaseConnectionSucceedsForGetAllAccounts() {
    when(householdMemberRepository.findAll()).thenReturn(
        Arrays.asList(expectedAccountOne, expectedAccountTwo,
            expectedAccountThree));

    when(accountRepository.findByHouseholdMemberId(HOUSEHOLD_MEMBER_ID_TO_BE_FOUND)).thenReturn(
        Arrays.asList(expectedAccountOne, expectedAccountTwo));
  }

  @And("the url does not contain query params for the get all accounts endpoint")
  public void theUrlDoesNotContainQueryParamsForTheGetAllAccountsEndpoint() {
    getAccountUrl = Constants.ACCOUNTS_URI;
  }

  @And("no accounts exist in the database for a given household member id")
  public void noAccountsExistInTheDatabaseForAGivenUserId() {
    when(householdMemberRepository.findByHouseholdMemberId(HOUSEHOLD_MEMBER_ID_TO_BE_FOUND))
        .thenReturn(Collections.emptyList());
  }

  @And("no accounts exist in the database")
  public void noAccountsExistInTheDatabase() {
    when(accountRepository.findAll()).thenReturn(Collections.emptyList());
  }

  @And("the household member id provided in the url is the incorrect format for the get accounts by id endpoint")
  public void theUserIdProvidedInTheUrlIsTheIncorrectFormatForTheGetHouseholdByIdEndpoint() {
    String invalidHouseholdMemberId = "abc";
    getAccountUrl = Constants.ACCOUNTS_URI + "?householdMemberId=" + invalidHouseholdMemberId;
  }

  @And("the service is not able to connect to the database for get by household member id accounts")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetByAccountIdAccounts() {
    when(householdMemberRepository.findByHouseholdMemberId(any()))
        .thenThrow(new DataProcessingException());
  }

  @And("the service is not able to connect to the database for get all accounts")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetAllAccounts() {
    when(householdMemberRepository.findAll())
        .thenThrow(new DataProcessingException());
  }

  @When("the get all accounts endpoint is invoked")
  public void theGetAllAccountsEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getAccountUrl))
        .andReturn();
  }

  @Then("the correct failure response is returned from the get all accounts endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAllAccountsEndpoint(
      DataTable dataTable) throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the get all accounts endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheGetAllAccountsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }

  @Then("the correct empty accounts response is returned")
  public void theCorrectEmptyAccountsResponseIsReturned() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct accounts are returned from the get all accounts endpoint")
  public void theCorrectAccountsAreReturnedFromTheGetAllAccountsEndpoint(
      DataTable dataTable)
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<Account> householdMembers =
        objectMapper.readerForListOf(Account.class).readValue(content);

    Assertions.assertEquals(
        Integer.parseInt(dataTable.column(0).getFirst()),
        householdMembers.size());

    Account householdMemberOne = householdMembers.get(0);
    AccountTestUtil.assertExpectedAgainstActual(expectedAccountOne,
        householdMemberOne);

    Account householdMemberTwo = householdMembers.get(1);
    AccountTestUtil.assertExpectedAgainstActual(expectedAccountTwo,
        householdMemberTwo);

    if (householdMembers.size() > 2) {
      Account householdMemberThree = householdMembers.get(2);
      AccountTestUtil.assertExpectedAgainstActual(expectedAccountThree,
          householdMemberThree);
    }
  }


}
