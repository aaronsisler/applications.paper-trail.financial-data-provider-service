package com.ebsolutions.papertrail.financialdataproviderservice.account;

import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.Account;
import com.ebsolutions.papertrail.financialdataproviderservice.model.HouseholdMember;
import com.ebsolutions.papertrail.financialdataproviderservice.model.Institution;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.HouseholdMemberTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.HouseholdTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.InstitutionTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.UserTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.util.AccountTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.ApiCallTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

@Slf4j
public class AccountSteps extends BaseStep {
  private final List<Integer> newlyCreateAccountIds = new ArrayList<>();

  private String requestContent;
  private RestClient.ResponseSpec response;
  private HouseholdMember householdMemberOne;
  private HouseholdMember householdMemberTwo;
  private Institution institution;
  private Account expectedAccountOne;
  private Account expectedAccountTwo;

  @And("a user exists in the database related to account creation")
  public void aUserExistsInTheDatabaseRelatedToAccountCreation() {
    // Nothing to do given test data setup, below for reference
    UserTestData.ACCOUNT_CREATE_ONE.get();
    UserTestData.ACCOUNT_CREATE_TWO.get();
  }

  @And("a household exist in the database related to account creation")
  public void aHouseholdExistInTheDatabaseRelatedToAccountCreation() {
    // Nothing to do given test data setup, below for reference
    HouseholdTestData.ACCOUNT_CREATE.get();
  }

  @And("an institution exist in the database related to account creation")
  public void anInstitutionExistInTheDatabaseRelatedToAccountCreation() {
    institution = InstitutionTestData.ACCOUNT_CREATE.get();
  }

  @And("two household members exist in the database related to account creation")
  public void twoHouseholdMembersExistInTheDatabaseRelatedToAccountCreation() {
    householdMemberOne = HouseholdMemberTestData.ACCOUNT_CREATE_ONE.get();
    householdMemberTwo = HouseholdMemberTestData.ACCOUNT_CREATE_TWO.get();
  }

  @And("a valid account with the institution and the first household member id is part of the request body for the create account endpoint")
  public void aValidAccountWithTheInstitutionAndTheFirstHouseholdMemberIdIsPartOfTheRequestBodyForTheCreateAccountEndpoint()
      throws JsonProcessingException {
    expectedAccountOne = Account.builder()
        .institutionId(institution.getId())
        .householdMemberId(householdMemberOne.getId())
        .name("create account name 1")
        .name("create account nickname 1")
        .build();

    requestContent = objectMapper.writeValueAsString(expectedAccountOne);
  }

  @And("a valid account with the institution and the second household member id is part of the request body for the create account endpoint")
  public void aValidAccountWithTheInstitutionAndTheSecondHouseholdMemberIdIsPartOfTheRequestBodyForTheCreateAccountEndpoint()
      throws JsonProcessingException {
    expectedAccountTwo = Account.builder()
        .institutionId(institution.getId())
        .householdMemberId(householdMemberTwo.getId())
        .name("create account name 2")
        .name("create account nickname 2")
        .build();

    requestContent = objectMapper.writeValueAsString(expectedAccountTwo);
  }

  @When("the create account endpoint is invoked")
  public void theCreateAccountEndpointIsInvoked() {
    response = ApiCallTestUtil.createThroughApi(restClient, TestConstants.ACCOUNTS_URI,
        requestContent);
  }

  @Then("the newly created account with the institution and the first household member is returned from the create account endpoint")
  public void theNewlyCreatedAccountWithTheInstitutionAndTheFirstHouseholdMemberIsReturnedFromTheCreateAccountEndpoint() {
    Account account = response.body(Account.class);

    Assertions.assertNotNull(account);

    AccountTestUtil
        .assertExpectedAgainstCreated(expectedAccountOne, account);

    newlyCreateAccountIds.add(account.getId());
  }

  @Then("the newly created account with the institution and the second household member is returned from the create account endpoint")
  public void theNewlyCreatedAccountWithTheInstitutionAndTheSecondHouseholdMemberIsReturnedFromTheCreateAccountEndpoint() {
    Account account = response.body(Account.class);

    Assertions.assertNotNull(account);

    AccountTestUtil
        .assertExpectedAgainstCreated(expectedAccountTwo, account);

    newlyCreateAccountIds.add(account.getId());
  }

  @When("the get all accounts endpoint is invoked")
  public void theGetAllAccountsEndpointIsInvoked() {
    response = ApiCallTestUtil.getThroughApi(restClient, TestConstants.ACCOUNTS_URI);
  }

  @Then("the correct accounts are returned from the get all accounts endpoint")
  public void theCorrectAccountsAreReturnedFromTheGetAllAccountsEndpoint() {
    List<Account> accounts = response.body(
        new ParameterizedTypeReference<ArrayList<Account>>() {
        });

    Assertions.assertNotNull(accounts);

    List<Account> createdAccounts =
        accounts.stream()
            .filter(account -> newlyCreateAccountIds.contains(
                account.getId())).toList();

    Assertions.assertEquals(2, createdAccounts.size());

    Account accountOne = createdAccounts.getFirst();
    AccountTestUtil
        .assertExpectedAgainstCreated(expectedAccountOne, accountOne);

    Account accountTwo = createdAccounts.getLast();
    AccountTestUtil
        .assertExpectedAgainstCreated(expectedAccountTwo, accountTwo);
  }

  @When("the get all accounts endpoint is invoked with the first household member id")
  public void theGetAllAccountsEndpointIsInvokedWithTheFirstHouseholdMemberId() {
    response = ApiCallTestUtil.getThroughApi(restClient,
        TestConstants.ACCOUNTS_URI + "?householdMemberId=" + householdMemberOne.getId());
  }

  @Then("the correct accounts are returned from the get all accounts endpoint invoked with the first household member id")
  public void theCorrectAccountsAreReturnedFromTheGetAllAccountsEndpointInvokedWithTheFirstHouseholdMemberId() {
    List<Account> accounts = response.body(
        new ParameterizedTypeReference<ArrayList<Account>>() {
        });

    Assertions.assertNotNull(accounts);

    Assertions.assertEquals(1, accounts.size());

    Account account = accounts.getFirst();
    AccountTestUtil
        .assertExpectedAgainstCreated(expectedAccountOne, account);
  }
}
