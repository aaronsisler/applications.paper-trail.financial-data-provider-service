package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.Account;
import com.ebsolutions.papertrail.financialdataproviderservice.model.AccountTransaction;
import com.ebsolutions.papertrail.financialdataproviderservice.testdata.AccountTestData;
import com.ebsolutions.papertrail.financialdataproviderservice.util.AccountTransactionTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.ApiCallTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.QueueMessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class AccountTransactionQueueSteps extends BaseStep {
  protected String endpoint = "http://sqs.us-east-1.localhost.localstack.cloud:4566";
  protected String queueUrl = endpoint + "/000000000000/ACCOUNT_TRANSACTION_INGESTION_DATAFLOW";
  protected QueueMessageUtil queueMessageUtil = new QueueMessageUtil(endpoint, queueUrl);

  protected AccountTransaction queueAccountTransaction;
  private RestClient.ResponseSpec response;
  private Account account;


  @And("a valid account transaction has a matching account in the data store")
  public void aValidAccountTransactionHasAMatchingAccountInTheDataStore() {
    account = AccountTestData.ACCOUNT_TRANSACTION_INGESTION.get();

    queueAccountTransaction = AccountTransaction.builder()
        .accountId(account.getId())
        .description("Chipotle")
        .amount(123)
        .transactionDate(LocalDate.now())
        .build();
  }

  @And("the account transaction is on the account transaction queue")
  public void theAccountTransactionIsOnTheAccountTransactionQueue() throws JsonProcessingException {
    String message = objectMapper.writeValueAsString(queueAccountTransaction);

    SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
        .queueUrl(queueUrl)
        .messageBody(message)
        .build();

    queueMessageUtil.produce(sendMessageRequest);
  }

  @When("the application tries to process the account transaction queue")
  public void theApplicationTriesToProcessTheAccountTransactionQueue() {
    // Nothing needed here since application is already up and listening to queue
  }

  @Then("the account transaction is available for retrieval")
  public void theAccountTransactionIsAvailableForRetrieval() throws InterruptedException {
    Instant pollingEnd =
        Instant.now().plusMillis(TestConstants.QUEUE_POLLING_WAIT_PERIOD_IN_MILLISECONDS);
    List<AccountTransaction> accountTransactions;
    do {
      try {
        Thread.sleep(100);
        response = ApiCallTestUtil.getThroughApi(restClient,
            TestConstants.ACCOUNT_TRANSACTIONS_URI + "?accountId=" + account.getId());

        accountTransactions = response.body(
            new ParameterizedTypeReference<ArrayList<AccountTransaction>>() {
            });

        if (accountTransactions != null) {
          break;
        }
        if (Instant.now().isAfter(pollingEnd)) {
          break;
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted while waiting for condition", e);
      }
    } while (!Instant.now().isAfter(pollingEnd));

    Assertions.assertNotNull(accountTransactions);

    Assertions.assertEquals(1, accountTransactions.size());

    AccountTransaction ingestedAccountTransaction = accountTransactions.getFirst();

    AccountTransactionTestUtil
        .assertExpectedAgainstCreated(queueAccountTransaction, ingestedAccountTransaction);
  }

  @And("the application removes the message from the queue")
  public void theApplicationRemovesTheMessageFromTheQueue() {
    // Check that the queue is empty
  }
}
