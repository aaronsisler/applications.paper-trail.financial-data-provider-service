package com.ebsolutions.papertrail.financialdataproviderservice;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountTransactionGetAllSteps {
  @And("two account transactions exist in the database for a given account id")
  public void twoAccountTransactionsExistInTheDatabaseForAGivenAccountId() {
  }

  @And("one account transaction exists in the database for a different account id")
  public void oneAccountTransactionExistsInTheDatabaseForADifferentAccountId() {
  }

  @And("the url does contain the account id query param for the get all account transactions endpoint")
  public void theUrlDoesContainTheAccountIdQueryParamForTheGetAllAccountTransactionsEndpoint() {
  }

  @And("the database connection succeeds for get all account transactions")
  public void theDatabaseConnectionSucceedsForGetAllAccountTransactions() {
  }

  @And("the url does not contain query params for the get all account transactions endpoint")
  public void theUrlDoesNotContainQueryParamsForTheGetAllAccountTransactionsEndpoint() {
  }

  @And("no account transactions exist in the database for a given account id")
  public void noAccountTransactionsExistInTheDatabaseForAGivenAccountId() {
  }

  @And("no account transactions exist in the database")
  public void noAccountTransactionsExistInTheDatabase() {
  }

  @And("the account id provided in the url is the incorrect format for the get household by id endpoint")
  public void theAccountIdProvidedInTheUrlIsTheIncorrectFormatForTheGetHouseholdByIdEndpoint() {
  }

  @And("the service is not able to connect to the database for get all account transactions")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetAllAccountTransactions() {
  }

  @And("the service is not able to connect to the database for get by account id account transactions")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetByAccountIdAccountTransactions() {
  }

  @When("the get all account transactions endpoint is invoked")
  public void theGetAllAccountTransactionsEndpointIsInvoked() {
  }

  @Then("the correct account transactions are returned from the get all account transactions endpoint")
  public void theCorrectAccountTransactionsAreReturnedFromTheGetAllAccountTransactionsEndpoint(
      DataTable dataTable) {
  }

  @Then("the correct empty account transactions response is returned")
  public void theCorrectEmptyAccountTransactionsResponseIsReturned() {
  }

  @Then("the correct failure response is returned from the get all account transactions endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAllAccountTransactionsEndpoint(
      DataTable dataTable) {
  }
}
