package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ebsolutions.papertrail.financialdataproviderservice.account.Account;
import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.AccountTransactionTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class AccountTransactionCreateAllSteps extends BaseTest {
  private final AccountRepository accountRepository;
  private final AccountTransactionRepository accountTransactionRepository;

  private String requestContent;
  private MvcResult result;
  private AccountTransaction expectedAccountTransactionOne;
  private AccountTransaction expectedAccountTransactionTwo;

  @And("valid transactions with the same account id are part of the request body for the create transaction endpoint")
  public void validTransactionsWithTheSameAccountIdArePartOfTheRequestBodyForTheCreateTransactionEndpoint()
      throws JsonProcessingException {
    AccountTransaction inputAccountTransactionOne =
        AccountTransaction.builder()
            .accountId(1)
            .amount(123)
            .description("Account Transaction Description 1")
            .transactionDate(TEST_LOCAL_DATE)
            .build();

    AccountTransaction inputAccountTransactionTwo =
        AccountTransaction.builder()
            .accountId(1)
            .amount(456)
            .description("Account Transaction Description 2")
            .transactionDate(TEST_LOCAL_DATE)
            .build();

    expectedAccountTransactionOne =
        AccountTransaction.builder()
            .id(1)
            .accountId(1)
            .amount(123)
            .description("Account Transaction Description 1")
            .transactionDate(TEST_LOCAL_DATE)
            .build();

    expectedAccountTransactionTwo = AccountTransaction.builder()
        .id(2)
        .accountId(1)
        .amount(456)
        .description("Account Transaction Description 2")
        .transactionDate(TEST_LOCAL_DATE)
        .build();

    requestContent =
        objectMapper.writeValueAsString(
            Arrays.asList(inputAccountTransactionOne, inputAccountTransactionTwo));
  }

  @And("account id exists in the database for the transactions")
  public void accountIdExistsInTheDatabaseForTheTransactions() {
    when(accountRepository.findById(any())).thenReturn(Optional.of(Account.builder().build()));
  }

  @And("the database connection succeeds for create transactions")
  public void theDatabaseConnectionSucceedsForCreateTransactions() {
    when(accountTransactionRepository.saveAll(any())).thenReturn(Arrays.asList(
        expectedAccountTransactionOne,
        expectedAccountTransactionTwo));
  }

  @And("valid transactions with different account ids are part of the request body for the create transaction endpoint")
  public void validTransactionsWithDifferentAccountIdsArePartOfTheRequestBodyForTheCreateTransactionEndpoint()
      throws JsonProcessingException {
    AccountTransaction inputAccountTransactionOne = AccountTransaction.builder()
        .accountId(1)
        .amount(123)
        .description("Account Transaction Description 1")
        .transactionDate(TEST_LOCAL_DATE)
        .build();

    AccountTransaction inputAccountTransactionTwo = AccountTransaction.builder()
        .accountId(2)
        .amount(456)
        .description("Account Transaction Description 2")
        .transactionDate(TEST_LOCAL_DATE)
        .build();

    expectedAccountTransactionOne =
        AccountTransaction.builder()
            .id(1)
            .accountId(1)
            .amount(123)
            .description("Account Transaction Description 1")
            .transactionDate(TEST_LOCAL_DATE)
            .build();

    expectedAccountTransactionTwo = AccountTransaction.builder()
        .id(2)
        .accountId(2)
        .amount(456)
        .description("Account Transaction Description 2")
        .transactionDate(TEST_LOCAL_DATE)
        .build();

    requestContent =
        objectMapper.writeValueAsString(Arrays.asList(inputAccountTransactionOne,
            inputAccountTransactionTwo));

    when(accountTransactionRepository.saveAll(any())).thenReturn(Arrays.asList(
        expectedAccountTransactionOne,
        expectedAccountTransactionTwo));
  }

  @And("account id does not exist in the database for the transactions")
  public void accountIdDoesNotExistInTheDatabaseForTheTransactions() {
    when(accountRepository.findById(any())).thenReturn(Optional.empty());
  }

  @And("the database save fails given a account id was deleted during the create transaction database call")
  public void theDatabaseSaveFailsGivenAAccountIdWasDeletedDuringTheCreateTransactionDatabaseCall() {
    doThrow(DataIntegrityViolationException.class)
        .when(accountTransactionRepository).saveAll(any());
  }

  @And("a transaction in the request body has an invalid input")
  public void aTransactionInTheRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int accountTransactionId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    int accountId = dataTable.column(1).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(1).getFirst());

    int amount = dataTable.column(2).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(2).getFirst());

    LocalDate localDate = dataTable.column(3).getFirst() == null ? null :
        LocalDate.parse(dataTable.column(3).getFirst());

    String description = CommonTestUtil.isEmptyString(dataTable.column(4).getFirst());

    AccountTransaction inputAccountTransaction =
        AccountTransaction.builder()
            .id(accountTransactionId)
            .accountId(accountId)
            .amount(amount)
            .description(description)
            .transactionDate(localDate)
            .build();

    requestContent =
        objectMapper.writeValueAsString(Collections.singletonList(inputAccountTransaction));
  }

  @And("a transaction in the request body has an invalid input for the transaction date")
  public void aTransactionInTheRequestBodyHasAnInvalidInputForTheTransactionDate(
      DataTable dataTable)
      throws JsonProcessingException {
    AccountTransaction inputAccountTransactionOne = AccountTransaction.builder()
        .accountId(1)
        .amount(123)
        .description("Account Transaction Description 1")
        .transactionDate(TEST_LOCAL_DATE)
        .build();

    String invalidDate = dataTable.column(0).getFirst();

    requestContent =
        objectMapper
            .writeValueAsString(Collections.singletonList(inputAccountTransactionOne))
            .replace("[2025,4,13]", invalidDate);
  }

  @And("the connection to the database fails for the get account by id")
  public void theConnectionToTheDatabaseFailsForTheGetAccountById() {
    when(accountRepository.findById(any())).thenThrow(new DataProcessingException());
  }

  @And("the connection to the database fails for the create transactions endpoint")
  public void theConnectionToTheDatabaseFailsForTheCreateTransactionsEndpoint() {
    when(accountTransactionRepository.saveAll(any())).thenThrow(new DataProcessingException());
  }

  @When("the create transactions endpoint is invoked")
  public void theCreateTransactionsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.ACCOUNT_TRANSACTIONS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the newly created transactions are returned from the create transaction endpoint")
  public void theNewlyCreatedTransactionsAreReturnedFromTheCreateTransactionEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<AccountTransaction> accountTransactions =
        objectMapper.readerForListOf(AccountTransaction.class).readValue(content);

    AccountTransaction accountTransactionOne = accountTransactions.getFirst();
    AccountTransactionTestUtil.assertExpectedAgainstActual(expectedAccountTransactionOne,
        accountTransactionOne);

    AccountTransaction accountTransactionTwo = accountTransactions.getLast();
    AccountTransactionTestUtil.assertExpectedAgainstActual(expectedAccountTransactionTwo,
        accountTransactionTwo);
  }

  @Then("the correct bad request response is returned from the create transactions endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheCreateTransactionsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());

  }

  @And("the transaction is not created")
  public void theTransactionIsNotCreated() {
    Mockito.verifyNoInteractions(accountTransactionRepository);
  }
}
