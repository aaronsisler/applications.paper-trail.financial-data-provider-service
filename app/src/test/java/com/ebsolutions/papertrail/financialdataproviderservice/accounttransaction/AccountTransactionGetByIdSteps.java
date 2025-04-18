package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountTransactionGetByIdSteps extends BaseTest {
  private static final int ACCOUNT_TRANSACTION_ID = 1;

  protected final AccountTransactionRepository accountTransactionRepository;
  private MvcResult result;
  private AccountTransaction expectedAccountTransaction;
  private String getAccountTransactionByIdUrl;

  @And("the requested account transaction exist in the database")
  public void theRequestedAccountTransactionExistInTheDatabase() {
    expectedAccountTransaction =
        AccountTransaction.builder()
            .id(ACCOUNT_TRANSACTION_ID)
            .accountId(123)
            .amount(456)
            .description("Account Transaction 1")
            .build();

    when(accountTransactionRepository.findById((long) ACCOUNT_TRANSACTION_ID)).thenReturn(
        Optional.ofNullable(expectedAccountTransaction));
  }

  @And("the account transaction id provided in the url is the incorrect format for the get account transaction by id endpoint")
  public void theAccountTransactionIdProvidedInTheUrlIsTheIncorrectFormatForTheGetAccountTransactionByIdEndpoint() {
    String invalidAccountTransactionId = "abc";
    getAccountTransactionByIdUrl =
        Constants.ACCOUNT_TRANSACTIONS_URI + "/" + invalidAccountTransactionId;
  }

  @And("the account transaction id provided in the url is the correct format for the get account transaction by id endpoint")
  public void theAccountTransactionIdProvidedInTheUrlIsTheCorrectFormatForTheGetAccountTransactionByIdEndpoint() {
    getAccountTransactionByIdUrl =
        Constants.ACCOUNT_TRANSACTIONS_URI + "/" + ACCOUNT_TRANSACTION_ID;
  }

  @And("the connection to the database fails for the get account transaction by id")
  public void theConnectionToTheDatabaseFailsForTheGetAccountTransactionById() {
    doThrow(new DataProcessingException())
        .when(accountTransactionRepository).findById(any());
  }

  @When("the get account transaction by id endpoint is invoked")
  public void theGetAccountTransactionByIdEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getAccountTransactionByIdUrl))
        .andReturn();
  }

  @Then("the correct account transaction are returned from the get account transaction by id endpoint")
  public void theCorrectAccountTransactionAreReturnedFromTheGetAccountTransactionByIdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    AccountTransaction accountTransaction =
        objectMapper.readValue(content, AccountTransaction.class);

    AccountTransactionTestUtil.assertExpectedAgainstActual(expectedAccountTransaction,
        accountTransaction);
  }

  @Then("the correct empty response is returned from the get account transaction by id endpoint")
  public void theCorrectEmptyResponseIsReturnedFromTheGetAccountTransactionByIdEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct failure response is returned from the get account transaction by id endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAccountTransactionByIdEndpoint(
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
