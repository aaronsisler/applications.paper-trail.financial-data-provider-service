package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction.AccountTransaction;
import com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction.AccountTransactionRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.AccountTransactionTestUtil;
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
public class AccountTransactionGetAllSteps extends BaseTest {
  private static final int ACCOUNT_ID_TO_BE_FOUND = 123;


  private final AccountRepository accountRepository;
  private final AccountTransactionRepository accountTransactionRepository;

  private String requestContent;
  private MvcResult result;
  private AccountTransaction expectedAccountTransactionOne;
  private AccountTransaction expectedAccountTransactionTwo;
  private AccountTransaction expectedAccountTransactionThree;
  private String getAccountTransactionsUrl;

  @BeforeEach
  public void setup() {
    expectedAccountTransactionOne = null;
    expectedAccountTransactionTwo = null;
    expectedAccountTransactionThree = null;
  }

  @And("two account transactions exist in the database for a given account id")
  public void twoAccountTransactionsExistInTheDatabaseForAGivenAccountId() {
    expectedAccountTransactionOne =
        AccountTransaction.builder()
            .id(1)
            .accountId(ACCOUNT_ID_TO_BE_FOUND)
            .amount(123)
            .description("Account Transaction 1")
            .build();

    expectedAccountTransactionTwo =
        AccountTransaction.builder()
            .id(2)
            .accountId(ACCOUNT_ID_TO_BE_FOUND)
            .amount(234)
            .description("Account Transaction 2")
            .build();
  }

  @And("one account transaction exists in the database for a different account id")
  public void oneAccountTransactionExistsInTheDatabaseForADifferentAccountId() {
    expectedAccountTransactionThree =
        AccountTransaction.builder()
            .id(3)
            .accountId(456)
            .amount(345)
            .description("Account Transaction 3")
            .build();
  }

  @And("the url does contain the account id query param for the get all account transactions endpoint")
  public void theUrlDoesContainTheAccountIdQueryParamForTheGetAllAccountTransactionsEndpoint() {
    getAccountTransactionsUrl =
        Constants.ACCOUNT_TRANSACTIONS_URI + "?accountId=" + ACCOUNT_ID_TO_BE_FOUND;

  }

  @And("the database connection succeeds for get all account transactions")
  public void theDatabaseConnectionSucceedsForGetAllAccountTransactions() {
    when(accountTransactionRepository.findAll()).thenReturn(
        Arrays.asList(expectedAccountTransactionOne, expectedAccountTransactionTwo,
            expectedAccountTransactionThree));

    when(accountTransactionRepository.findByAccountId(ACCOUNT_ID_TO_BE_FOUND)).thenReturn(
        Arrays.asList(expectedAccountTransactionOne, expectedAccountTransactionTwo));
  }

  @And("the url does not contain query params for the get all account transactions endpoint")
  public void theUrlDoesNotContainQueryParamsForTheGetAllAccountTransactionsEndpoint() {
    getAccountTransactionsUrl = Constants.ACCOUNT_TRANSACTIONS_URI;
  }

  @And("no account transactions exist in the database for a given account id")
  public void noAccountTransactionsExistInTheDatabaseForAGivenAccountId() {
    when(accountTransactionRepository.findByAccountId(ACCOUNT_ID_TO_BE_FOUND))
        .thenReturn(Collections.emptyList());
  }

  @And("no account transactions exist in the database")
  public void noAccountTransactionsExistInTheDatabase() {
    when(accountTransactionRepository.findAll())
        .thenReturn(Collections.emptyList());
  }

  @And("the account id provided in the url is the incorrect format for the get household by id endpoint")
  public void theAccountIdProvidedInTheUrlIsTheIncorrectFormatForTheGetHouseholdByIdEndpoint() {
    String invalidAccountId = "abc";
    getAccountTransactionsUrl =
        Constants.ACCOUNT_TRANSACTIONS_URI + "?accountId=" + invalidAccountId;
  }

  @And("the service is not able to connect to the database for get all account transactions")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetAllAccountTransactions() {
    when(accountTransactionRepository.findAll()).thenThrow(new DataProcessingException());
  }

  @And("the service is not able to connect to the database for get by account id account transactions")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetByAccountIdAccountTransactions() {
    when(accountTransactionRepository.findByAccountId(any()))
        .thenThrow(new DataProcessingException());
  }

  @When("the get all account transactions endpoint is invoked")
  public void theGetAllAccountTransactionsEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getAccountTransactionsUrl))
        .andReturn();
  }

  @Then("the correct empty account transactions response is returned")
  public void theCorrectEmptyAccountTransactionsResponseIsReturned() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct failure response is returned from the get all account transactions endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAllAccountTransactionsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }

  @Then("the correct account transactions are returned from the get all account transactions endpoint")
  public void theCorrectAccountTransactionsAreReturnedFromTheGetAllAccountTransactionsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<AccountTransaction> accountTransactions =
        objectMapper.readerForListOf(AccountTransaction.class).readValue(content);

    Assertions.assertEquals(
        Integer.parseInt(dataTable.column(0).getFirst()),
        accountTransactions.size());

    AccountTransaction accountTransactionOne = accountTransactions.get(0);
    AccountTransactionTestUtil.assertExpectedAgainstActual(expectedAccountTransactionOne,
        accountTransactionOne);

    AccountTransaction accountTransactionTwo = accountTransactions.get(1);
    AccountTransactionTestUtil.assertExpectedAgainstActual(expectedAccountTransactionTwo,
        accountTransactionTwo);

    if (accountTransactions.size() > 2) {
      AccountTransaction accountTransactionThree = accountTransactions.get(2);
      AccountTransactionTestUtil.assertExpectedAgainstActual(expectedAccountTransactionThree,
          accountTransactionThree);
    }
  }
}
