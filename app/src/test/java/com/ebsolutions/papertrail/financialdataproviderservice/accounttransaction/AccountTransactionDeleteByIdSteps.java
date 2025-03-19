package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountTransactionDeleteByIdSteps extends BaseTest {
  protected final AccountTransactionRepository accountTransactionRepository;
  private final Integer validAccountTransactionId = 1;
  private String deleteAccountTransactionsUrl;
  private MvcResult result;

  @And("the account transaction id provided in the url is the correct format")
  public void theAccountTransactionIdProvidedInTheUrlIsTheCorrectFormat() {
    deleteAccountTransactionsUrl =
        Constants.ACCOUNT_TRANSACTIONS_URI + "/" + validAccountTransactionId;
  }

  @And("the correct account transaction is deleted")
  public void theCorrectAccountTransactionIsDeleted() {
    Mockito.verify(accountTransactionRepository).deleteById(validAccountTransactionId.longValue());
  }

  @And("the account transaction id provided in the url is the incorrect format")
  public void theAccountTransactionIdProvidedInTheUrlIsTheIncorrectFormat() {
    String invalidAccountTransactionId = "abc";
    deleteAccountTransactionsUrl =
        Constants.ACCOUNT_TRANSACTIONS_URI + "/" + invalidAccountTransactionId;
  }

  @And("the connection to the database fails for the delete account transaction endpoint")
  public void theConnectionToTheDatabaseFailsForTheDeleteAccountTransactionEndpoint() {
    doThrow(new DataProcessingException())
        .when(accountTransactionRepository).deleteById(any());
  }

  @When("the delete account transaction endpoint is invoked")
  public void theDeleteAccountTransactionEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(delete(deleteAccountTransactionsUrl))
        .andReturn();
  }

  @Then("the correct response is returned from the delete account transaction endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteAccountTransactionEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }


  @Then("the correct failure response is returned from the delete account transaction endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheDeleteAccountTransactionEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());

  }

}
