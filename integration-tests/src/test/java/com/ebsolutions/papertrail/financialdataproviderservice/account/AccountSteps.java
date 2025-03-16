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
import org.junit.jupiter.api.Assertions;
import org.springframework.web.client.RestClient;

public class AccountSteps extends BaseStep {
  private final List<Integer> newlyCreateAccountIds = new ArrayList<>();

  private String requestContent;
  private RestClient.ResponseSpec response;
  private HouseholdMember householdMember;
  private Institution institution;
  private Account expectedAccount;

  @And("a user exists in the database related to account creation")
  public void aUserExistsInTheDatabaseRelatedToAccountCreation() {
    // Nothing to do given test data setup, below for reference
    UserTestData.ACCOUNT_CREATE.get();
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

  @And("a household member exist in the database related to account creation")
  public void aHouseholdMemberExistInTheDatabaseRelatedToAccountCreation() {
    householdMember = HouseholdMemberTestData.ACCOUNT_CREATE.get();
  }

  @And("a valid account with the institution and household member is part of the request body for the create account endpoint")
  public void aValidAccountWithTheInstitutionAndHouseholdMemberIsPartOfTheRequestBodyForTheCreateAccountEndpoint()
      throws JsonProcessingException {
    expectedAccount = Account.builder()
        .institutionId(institution.getId())
        .householdMemberId(householdMember.getId())
        .name("create account name")
        .name("create account nickname")
        .build();

    requestContent = objectMapper.writeValueAsString(expectedAccount);

  }

  @When("the create account endpoint is invoked")
  public void theCreateAccountEndpointIsInvoked() {
    response = ApiCallTestUtil.createThroughApi(restClient, TestConstants.ACCOUNTS_URI,
        requestContent);
  }

  @Then("the newly created account with the institution and household member is returned from the create account endpoint")
  public void theNewlyCreatedAccountWithTheInstitutionAndHouseholdMemberIsReturnedFromTheCreateAccountEndpoint() {
    Account account = response.body(Account.class);

    Assertions.assertNotNull(account);

    AccountTestUtil
        .assertExpectedAgainstCreated(expectedAccount, account);

    newlyCreateAccountIds.add(householdMember.getId());
  }
}
