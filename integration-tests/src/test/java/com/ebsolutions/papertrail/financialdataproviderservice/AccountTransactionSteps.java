package com.ebsolutions.papertrail.financialdataproviderservice;

import com.ebsolutions.papertrail.financialdataproviderservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.Account;
import com.ebsolutions.papertrail.financialdataproviderservice.model.AccountTransaction;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.AccountTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.AccountTransactionTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.HouseholdMemberTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.HouseholdTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.InstitutionTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.UserTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.util.AccountTransactionTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.ApiCallTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class AccountTransactionSteps extends BaseStep {
  private final List<Integer> newlyCreateAccountTransactionIds = new ArrayList<>();

  private String requestContent;
  private RestClient.ResponseSpec response;
  private int resultAccountTransactionId;
  private String accountTransactionByIdUrl;

  private Account accountOne;
  private Account accountTwo;

  private AccountTransaction expectedAccountTransactionOne;
  private AccountTransaction expectedAccountTransactionTwo;
  private AccountTransaction updatedAccountTransaction;


  @And("a user exists in the database")
  public void aUserExistsInTheDatabase() {
    // Nothing to do given test data setup
    UserTestData.ACCOUNT_TRANSACTION_CREATE.get();
  }

  @And("a household exists in the database")
  public void aHouseholdExistsInTheDatabase() {
    // Nothing to do given test data setup
    HouseholdTestData.ACCOUNT_TRANSACTION_CREATE.get();
  }

  @And("a household member for the user and household exists in the database")
  public void aHouseholdMemberForTheUserAndHouseholdExistsInTheDatabase() {
    // Nothing to do given test data setup
    HouseholdMemberTestData.ACCOUNT_TRANSACTION_CREATE.get();
  }

  @And("an institution exists in the database")
  public void anInstitutionExistsInTheDatabase() {
    // Nothing to do given test data setup
    InstitutionTestData.ACCOUNT_CREATE.get();
  }

  @And("two accounts with different account ids for the institution and household member exists in the database")
  public void twoAccountsWithDifferentAccountIdsForTheInstitutionAndHouseholdMemberExistsInTheDatabase() {
    // Nothing to do given test data setup
    accountOne = AccountTestData.ACCOUNT_TRANSACTION_CREATE_ONE.get();
    accountTwo = AccountTestData.ACCOUNT_TRANSACTION_CREATE_TWO.get();
  }

  @And("a valid account transaction with the first account id is part of the request body for the create account transactions endpoint")
  public void aValidAccountTransactionWithTheFirstAccountIdIsPartOfTheRequestBodyForTheCreateAccountTransactionsEndpoint()
      throws JsonProcessingException {
    expectedAccountTransactionOne =
        AccountTransaction.builder()
            .accountId(accountOne.getId())
            .amount(123)
            .description("Account Transaction 1")
            .build();

    requestContent =
        objectMapper.writeValueAsString(Collections.singletonList(expectedAccountTransactionOne));
  }

  @And("a valid account transaction with the second account id is part of the request body for the create account transactions endpoint")
  public void aValidAccountTransactionWithTheSecondAccountIdIsPartOfTheRequestBodyForTheCreateAccountTransactionsEndpoint()
      throws JsonProcessingException {
    expectedAccountTransactionTwo =
        AccountTransaction.builder()
            .accountId(accountTwo.getId())
            .amount(456)
            .description("Account Transaction 2")
            .build();

    requestContent =
        objectMapper.writeValueAsString(Collections.singletonList(expectedAccountTransactionTwo));
  }

  @When("the create account transactions endpoint is invoked")
  public void theCreateAccountTransactionsEndpointIsInvoked() {
    response = ApiCallTestUtil.createThroughApi(restClient, TestConstants.ACCOUNT_TRANSACTIONS_URI,
        requestContent);
  }

  @When("the get all account transactions endpoint is invoked")
  public void theGetAllAccountTransactionsEndpointIsInvoked() {
    response = ApiCallTestUtil.getThroughApi(restClient, TestConstants.ACCOUNT_TRANSACTIONS_URI);
  }

  @When("the get all account transactions endpoint is invoked with the first account id")
  public void theGetAllAccountTransactionsEndpointIsInvokedWithTheFirstAccountId() {
    response = ApiCallTestUtil.getThroughApi(restClient,
        TestConstants.ACCOUNT_TRANSACTIONS_URI + "?accountId=" + accountOne.getId());
  }

  @Then("the newly created account transaction with the first account id is returned from the create account transactions endpoint")
  public void theNewlyCreatedAccountTransactionWithTheFirstAccountIdIsReturnedFromTheCreateAccountTransactionsEndpoint() {
    List<AccountTransaction> accountTransactions = response.body(
        new ParameterizedTypeReference<ArrayList<AccountTransaction>>() {
        });

    Assertions.assertNotNull(accountTransactions);
    Assertions.assertEquals(1, accountTransactions.size());

    AccountTransaction accountTransaction = accountTransactions.getFirst();

    AccountTransactionTestUtil
        .assertExpectedAgainstCreated(expectedAccountTransactionOne, accountTransaction);

    newlyCreateAccountTransactionIds.add(accountTransaction.getId());
  }

  @Then("the newly created account transaction with the second account id is returned from the create account transactions endpoint")
  public void theNewlyCreatedAccountTransactionWithTheSecondAccountIdIsReturnedFromTheCreateAccountTransactionsEndpoint() {
    List<AccountTransaction> accountTransactions = response.body(
        new ParameterizedTypeReference<ArrayList<AccountTransaction>>() {
        });

    Assertions.assertNotNull(accountTransactions);
    Assertions.assertEquals(1, accountTransactions.size());

    AccountTransaction accountTransaction = accountTransactions.getFirst();

    AccountTransactionTestUtil
        .assertExpectedAgainstCreated(expectedAccountTransactionTwo, accountTransaction);

    newlyCreateAccountTransactionIds.add(accountTransaction.getId());
  }

  @Then("the correct account transactions are returned from the get all account transactions endpoint")
  public void theCorrectAccountTransactionsAreReturnedFromTheGetAllAccountTransactionsEndpoint() {
    List<AccountTransaction> accountTransactions = response.body(
        new ParameterizedTypeReference<ArrayList<AccountTransaction>>() {
        });

    Assertions.assertNotNull(accountTransactions);

    List<AccountTransaction> createdAccountTransactions =
        accountTransactions.stream()
            .filter(accountTransaction -> newlyCreateAccountTransactionIds.contains(
                accountTransaction.getId())).toList();

    Assertions.assertEquals(2, createdAccountTransactions.size());

    AccountTransaction accountTransactionOne = createdAccountTransactions.getFirst();

    AccountTransactionTestUtil
        .assertExpectedAgainstCreated(expectedAccountTransactionOne, accountTransactionOne);

    AccountTransaction accountTransactionTwo = createdAccountTransactions.getLast();
    AccountTransactionTestUtil
        .assertExpectedAgainstCreated(expectedAccountTransactionTwo, accountTransactionTwo);
  }

  @Then("the correct account transactions are returned from the get all account transactions endpoint invoked with the first account id")
  public void theCorrectAccountTransactionsAreReturnedFromTheGetAllAccountTransactionsEndpointInvokedWithTheFirstAccountId() {
    List<AccountTransaction> accountTransactions = response.body(
        new ParameterizedTypeReference<ArrayList<AccountTransaction>>() {
        });

    Assertions.assertNotNull(accountTransactions);

    Assertions.assertEquals(1, accountTransactions.size());

    AccountTransaction accountTransaction = accountTransactions.getFirst();
    AccountTransactionTestUtil
        .assertExpectedAgainstCreated(expectedAccountTransactionOne, accountTransaction);
  }

  @And("the update account transaction id provided exists in the database")
  public void theUpdateAccountTransactionIdProvidedExistsInTheDatabase() {
    AccountTransaction databaseSetupAccountTransaction =
        AccountTransactionTestData.ACCOUNT_TRANSACTION_UPDATE.get();

    Assertions.assertNotNull(databaseSetupAccountTransaction);

    if (databaseSetupAccountTransaction.getId() == null) {
      Assertions.fail("Data setup failed for account transaction");
    }

    resultAccountTransactionId = databaseSetupAccountTransaction.getId();
    accountTransactionByIdUrl =
        TestConstants.ACCOUNT_TRANSACTIONS_URI + "/" + resultAccountTransactionId;

    response = ApiCallTestUtil.getThroughApi(restClient, accountTransactionByIdUrl);

    AccountTransaction retrievedCreatedAccountTransaction = response.body(AccountTransaction.class);

    Assertions.assertNotNull(retrievedCreatedAccountTransaction);

    AccountTransactionTestUtil.assertExpectedAgainstActual(
        databaseSetupAccountTransaction,
        retrievedCreatedAccountTransaction);
  }

  @And("an update for the account transaction is valid and part of the request body for the update account transaction endpoint")
  public void anUpdateForTheAccountTransactionIsValidAndPartOfTheRequestBodyForTheUpdateAccountTransactionEndpoint()
      throws JsonProcessingException {

    updatedAccountTransaction =
        AccountTransaction.builder()
            .id(resultAccountTransactionId)
            .accountId(accountOne.getId())
            .amount(789)
            .description("updated_description")
            .build();

    requestContent =
        objectMapper.writeValueAsString(updatedAccountTransaction);
  }

  @When("the update account transaction endpoint is invoked")
  public void theUpdateAccountTransactionEndpointIsInvoked() {
    response = ApiCallTestUtil.updateThroughApi(restClient, TestConstants.ACCOUNT_TRANSACTIONS_URI,
        requestContent);
  }

  @Then("the updated account transaction is returned from the update account transaction endpoint")
  public void theUpdatedAccountTransactionIsReturnedFromTheUpdateAccountTransactionEndpoint() {
    AccountTransaction returnedUpdatedAccountTransaction = response.body(AccountTransaction.class);

    Assertions.assertNotNull(returnedUpdatedAccountTransaction);

    AccountTransactionTestUtil.assertExpectedAgainstActual(
        updatedAccountTransaction,
        returnedUpdatedAccountTransaction);
  }

  @And("the updated account transaction is correct in the database")
  public void theUpdatedAccountTransactionIsCorrectInTheDatabase() {
    response = ApiCallTestUtil.getThroughApi(restClient, accountTransactionByIdUrl);

    AccountTransaction retrievedUpdatedAccountTransaction = response.body(AccountTransaction.class);

    Assertions.assertNotNull(retrievedUpdatedAccountTransaction);

    AccountTransactionTestUtil
        .assertExpectedAgainstActual(updatedAccountTransaction, retrievedUpdatedAccountTransaction);
  }

  @And("the delete account transaction id provided exists in the database")
  public void theDeleteAccountTransactionIdProvidedExistsInTheDatabase() {
    AccountTransaction databaseSetupAccountTransaction =
        AccountTransactionTestData.ACCOUNT_TRANSACTION_DELETE.get();

    Assertions.assertNotNull(databaseSetupAccountTransaction);
    if (databaseSetupAccountTransaction.getId() == null) {
      Assertions.fail("Data setup failed for account transaction");
    }

    resultAccountTransactionId = databaseSetupAccountTransaction.getId();
    accountTransactionByIdUrl =
        TestConstants.ACCOUNT_TRANSACTIONS_URI + "/" + resultAccountTransactionId;

    response = ApiCallTestUtil.getThroughApi(restClient, accountTransactionByIdUrl);

    AccountTransaction retrievedCreatedAccountTransaction = response.body(AccountTransaction.class);

    Assertions.assertNotNull(retrievedCreatedAccountTransaction);

    AccountTransactionTestUtil
        .assertExpectedAgainstActual(
            databaseSetupAccountTransaction,
            retrievedCreatedAccountTransaction);
  }

  @When("the delete account transaction endpoint is invoked")
  public void theDeleteAccountTransactionEndpointIsInvoked() {
    response = ApiCallTestUtil.deleteThroughApi(restClient, accountTransactionByIdUrl);
  }

  @Then("the correct response is returned from the delete account transaction endpoint")
  public void theCorrectResponseIsReturnedFromTheDeleteAccountTransactionEndpoint() {
    ApiCallTestUtil.checkForNoContentStatusCode(response);
  }

  @And("the correct account transaction is deleted")
  public void theCorrectAccountTransactionIsDeleted() {
    response = ApiCallTestUtil.getThroughApi(restClient, accountTransactionByIdUrl);

    ApiCallTestUtil.checkForNoContentStatusCode(response);
  }
}
