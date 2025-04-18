package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;

import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@RequiredArgsConstructor
public class AccountTransactionQueueSubscriberSteps extends BaseTest {
  protected final AccountTransactionRepository accountTransactionRepository;
  protected final AccountRepository accountRepository;
  protected final AccountTransactionQueueSubscriber accountTransactionQueueSubscriber;
  protected final SqsClient sqsClient;

  private String queueContent;

  @And("the application is not able to receive messages from the account transaction queue")
  public void theApplicationIsNotAbleToReceiveMessagesFromTheAccountTransactionQueue() {
    doThrow(SqsException.builder().build()).when(sqsClient)
        .receiveMessage(any(ReceiveMessageRequest.class));
  }

  @When("the application tries to process the account transaction queue")
  public void theApplicationTriesToProcessTheAccountTransactionQueue() {
    accountTransactionQueueSubscriber.consumeMessages();
  }

  @Then("the application does not save any account transaction")
  public void theApplicationDoesNotSaveAnyAccountTransaction() {
    Mockito.verifyNoInteractions(accountTransactionRepository);
  }
}
