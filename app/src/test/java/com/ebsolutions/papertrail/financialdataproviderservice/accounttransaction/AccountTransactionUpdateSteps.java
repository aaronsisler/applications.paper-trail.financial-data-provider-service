package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountTransactionUpdateSteps extends BaseTest {
  protected final AccountTransactionRepository accountTransactionRepository;
  protected final AccountRepository accountRepository;

  private String requestContent;
  private MvcResult result;
  private AccountTransaction expectedAccountTransaction;


  @And("the account transaction is part of the request body for the update account transaction endpoint")
  public void theAccountTransactionIsPartOfTheRequestBodyForTheUpdateAccountTransactionEndpoint()
      throws JsonProcessingException {
    AccountTransaction inputAccountTransaction =
        AccountTransaction.builder()
            .id(1)
            .accountId(147)
            .amount(123)
            .description("Account Transaction Description 1")
            .transactionDate(TEST_LOCAL_DATE)
            .build();

    expectedAccountTransaction =
        AccountTransaction.builder()
            .id(1)
            .accountId(147)
            .amount(123)
            .description("Updated Account Transaction Description 1")
            .transactionDate(TEST_LOCAL_DATE)
            .build();

    requestContent =
        objectMapper.writeValueAsString(inputAccountTransaction);
  }

  @And("the account transaction id exists in the database")
  public void theAccountTransactionIdExistsInTheDatabase() {
    when(accountTransactionRepository.findById(anyLong()))
        .thenReturn(Optional.of(AccountTransaction.builder().build()));
  }

  @And("the account transaction id does not exist in the database")
  public void theAccountTransactionIdDoesNotExistInTheDatabase() {
    when(accountTransactionRepository.findById(anyLong()))
        .thenReturn(Optional.empty());
  }

  @And("the database connection succeeds for update account transaction")
  public void theDatabaseConnectionSucceedsForUpdateAccountTransaction() {
    when(accountTransactionRepository.save(any()))
        .thenReturn(expectedAccountTransaction);
  }

  @And("the account id exists in the database for the account transaction")
  public void theAccountIdExistsInTheDatabaseForTheAccountTransaction() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.of(Account.builder().build()));
  }

  @And("the account id does not exist in the database for the account transaction")
  public void theAccountIdDoesNotExistInTheDatabaseForTheAccountTransaction() {
    when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
  }

  @And("the account transaction in the update account transaction request body has an invalid input")
  public void theAccountTransactionInTheUpdateAccountTransactionRequestBodyHasAnInvalidInput(
      DataTable dataTable) throws JsonProcessingException {
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
            .transactionDate(localDate)
            .description(description)
            .build();

    requestContent =
        objectMapper.writeValueAsString(inputAccountTransaction);
  }

  @And("the connection to the database fails for the update account transaction endpoint")
  public void theConnectionToTheDatabaseFailsForTheUpdateAccountTransactionEndpoint() {
    when(accountTransactionRepository.save(any()))
        .thenThrow(new DataProcessingException());
  }

  @And("the connection to the database fails for the get account transaction by id within the account transaction endpoint")
  public void theConnectionToTheDatabaseFailsForTheGetAccountTransactionByIdWithinTheAccountTransactionEndpoint() {
    when(accountTransactionRepository.findById(anyLong()))
        .thenThrow(new DataProcessingException());
  }

  @When("the update account transaction endpoint is invoked")
  public void theUpdateAccountTransactionEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(put(Constants.ACCOUNT_TRANSACTIONS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the correct failure response and message is returned from the update account transaction endpoint")
  public void theCorrectFailureResponseAndMessageIsReturnedFromTheUpdateAccountTransactionEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());

  }

  @Then("the updated account transaction is returned from the update account transaction endpoint")
  public void theUpdatedAccountTransactionIsReturnedFromTheUpdateAccountTransactionEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    AccountTransaction accountTransaction =
        objectMapper.readValue(content, AccountTransaction.class);

    AccountTransactionTestUtil.assertExpectedAgainstActual(
        expectedAccountTransaction,
        accountTransaction);
  }
}
