package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

@RequiredArgsConstructor
public class AccountTransactionQueueSubscriberSteps extends BaseTest {
  protected final AccountTransactionRepository accountTransactionRepository;
  protected final AccountRepository accountRepository;
  protected final AccountTransactionQueue accountTransactionQueue;
  protected final AccountTransactionQueueSubscriber accountTransactionQueueSubscriber;
  protected final SqsClient sqsClient;
  protected ReceiveMessageResponse receiveMessageResponse;
  protected List<Message> messages = new ArrayList<>();


  private String queueContent;

  @And("the application is not able to receive messages from the account transaction queue")
  public void theApplicationIsNotAbleToReceiveMessagesFromTheAccountTransactionQueue() {
    doThrow(SqsException.builder().build()).when(sqsClient)
        .receiveMessage(any(ReceiveMessageRequest.class));
  }

  @And("the application is able to receive messages from the account transaction queue")
  public void theApplicationIsAbleToReceiveMessagesFromTheAccountTransactionQueue() {
    when(accountTransactionQueue.getQueueUrl()).thenReturn("correct_queue_url");
    when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
        .thenReturn(receiveMessageResponse);
  }

  @And("the account transaction queue does not have any messages")
  public void theAccountTransactionQueueDoesNotHaveAnyMessages() {
    receiveMessageResponse = ReceiveMessageResponse.builder().messages(messages).build();
  }

  @And("the account transaction queue has a message that is not able to be parsed")
  public void theAccountTransactionQueueHasAMessageThatIsNotAbleToBeParsed() {
    Message message = Message.builder().body("I AM A BAD MESSAGE").build();

    messages.add(message);
    receiveMessageResponse = ReceiveMessageResponse.builder().messages(messages).build();
  }

  @When("the application tries to process the account transaction queue")
  public void theApplicationTriesToProcessTheAccountTransactionQueue() throws InterruptedException {
    accountTransactionQueueSubscriber.consumeMessages();
  }

  @Then("the application does not save any account transaction")
  public void theApplicationDoesNotSaveAnyAccountTransaction() {
    Mockito.verifyNoInteractions(accountTransactionRepository);
  }

  @And("the message is deleted from the account transaction queue")
  public void theMessageIsDeletedFromTheAccountTransactionQueue() {
    ArgumentCaptor<DeleteMessageBatchRequest> argumentCaptor =
        ArgumentCaptor.forClass(DeleteMessageBatchRequest.class);

    Mockito.verify(sqsClient, times(1)).deleteMessageBatch(argumentCaptor.capture());

    Assertions.assertEquals("correct_queue_url", argumentCaptor.getValue().queueUrl());

    Assertions.assertEquals(1, argumentCaptor.getValue().entries().size());
  }
}
