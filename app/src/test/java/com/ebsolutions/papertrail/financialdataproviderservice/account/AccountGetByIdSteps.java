package com.ebsolutions.papertrail.financialdataproviderservice.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountGetByIdSteps extends BaseTest {
  private static final int ACCOUNT_ID = 1;

  protected final AccountRepository accountRepository;
  private MvcResult result;
  private Account expectedAccount;
  private String getAccountByIdUrl;

  @And("the requested account exist in the database")
  public void theRequestedAccountExistInTheDatabase() {
    expectedAccount =
        Account.builder()
            .id(ACCOUNT_ID)
            .institutionId(123)
            .householdMemberId(456)
            .name("Valid Name")
            .nickname("Valid Nickname")
            .build();

    when(accountRepository.findById((long) ACCOUNT_ID))
        .thenReturn(Optional.ofNullable(expectedAccount));
  }

  @And("the account id provided in the url is the correct format for the get account by id endpoint")
  public void theAccountIdProvidedInTheUrlIsTheCorrectFormatForTheGetAccountByIdEndpoint() {
    getAccountByIdUrl = Constants.ACCOUNTS_URI + "/" + ACCOUNT_ID;
  }

  @And("the connection to the database fails for the get account by id")
  public void theConnectionToTheDatabaseFailsForTheGetAccountById() {
    doThrow(new DataProcessingException())
        .when(accountRepository).findById(any());
  }

  @When("the get account by id endpoint is invoked")
  public void theGetAccountByIdEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getAccountByIdUrl))
        .andReturn();
  }

  @Then("the correct account are returned from the get account by id endpoint")
  public void theCorrectAccountAreReturnedFromTheGetAccountByIdEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    Account account = objectMapper.readValue(content, Account.class);

    AccountTestUtil.assertExpectedAgainstActual(expectedAccount, account);
  }

  @Then("the correct empty response is returned from the get account by id endpoint")
  public void theCorrectEmptyResponseIsReturnedFromTheGetAccountByIdEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @And("the account id provided in the url is the incorrect format for the get account by id endpoint")
  public void theAccountIdProvidedInTheUrlIsTheIncorrectFormatForTheGetAccountByIdEndpoint() {
    String invalidAccountId = "abc";
    getAccountByIdUrl =
        Constants.ACCOUNTS_URI + "/" + invalidAccountId;
  }

  @Then("the correct failure response is returned from the get account by id endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAccountByIdEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());

  }
}
