package com.ebsolutions.papertrail.financialdataproviderservice.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountUpdateSteps extends BaseTest {
  private final AccountRepository accountRepository;

  private String requestContent;
  private MvcResult result;
  private Account expectedAccount;
  private Account persistedAccount;

  @And("an account is part of the request body for the update account endpoint")
  public void anAccountIsPartOfTheRequestBodyForTheUpdateAccountEndpoint()
      throws JsonProcessingException {
    Account inputAccount = Account
        .builder()
        .id(123)
        .householdMemberId(456)
        .institutionId(789)
        .name("Updated Input Account Name")
        .nickname("Update Input Account Nickname")
        .build();

    persistedAccount = Account
        .builder()
        .id(123)
        .householdMemberId(456)
        .institutionId(789)
        .name("Input Account Name")
        .nickname("Input Account Nickname")
        .build();

    expectedAccount =
        Account.builder()
            .id(123)
            .householdMemberId(456)
            .institutionId(789)
            .name("Updated Input Account Name")
            .nickname("Updated Input Account Nickname")
            .build();

    requestContent = objectMapper.writeValueAsString(inputAccount);
  }

  @And("the account in the update account request body has an invalid input")
  public void theAccountInTheUpdateAccountRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int accountId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    int householdMemberId = dataTable.column(1).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(1).getFirst());

    int institutionId = dataTable.column(2).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(2).getFirst());

    String name = CommonTestUtil.isEmptyString(dataTable.column(3).getFirst());

    String nickname = CommonTestUtil.isEmptyString(dataTable.column(4).getFirst());

    Account inputAccount =
        Account.builder()
            .id(accountId)
            .householdMemberId(householdMemberId)
            .institutionId(institutionId)
            .name(name)
            .nickname(nickname)
            .build();

    requestContent =
        objectMapper.writeValueAsString(inputAccount);
  }

  @And("a record with a matching account id resides in the database")
  public void aRecordWithAMatchingAccountIdResidesInTheDatabase() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.of(persistedAccount));
  }


  @And("the database connection succeeds for update account")
  public void theDatabaseConnectionSucceedsForUpdateAccount() {
    when(accountRepository.save(any())).thenReturn(expectedAccount);
  }

  @When("the update account endpoint is invoked")
  public void theUpdateAccountEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(put(Constants.ACCOUNTS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the updated account is returned from the update account endpoint")
  public void theUpdatedAccountIsReturnedFromTheUpdateAccountEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    System.out.println(mockHttpServletResponse);
    System.out.println(mockHttpServletResponse.getStatus());

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    Account account = objectMapper.readValue(content, Account.class);

    AccountTestUtil.assertExpectedAgainstActual(expectedAccount, account);
  }

  @Then("the correct failure response and message is returned from the update account endpoint")
  public void theCorrectFailureResponseAndMessageIsReturnedFromTheUpdateAccountEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());

  }
}
