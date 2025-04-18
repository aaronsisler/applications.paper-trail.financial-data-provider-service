package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ebsolutions.papertrail.financialdataproviderservice.account.Account;
import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchResponse;
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

  @And("the account transaction queue has a message that has a populated account transaction id")
  public void theAccountTransactionQueueHasAMessageThatHasAPopulatedAccountTransactionId()
      throws JsonProcessingException {

    AccountTransaction accountTransaction = AccountTransaction.builder()
        .id(123)
        .accountId(1)
        .transactionDate(LocalDate.now())
        .amount(123)
        .description("Valid description")
        .build();

    String messageContent = objectMapper.writeValueAsString(accountTransaction);

    Message message = Message.builder().body(messageContent).build();

    messages.add(message);
    receiveMessageResponse = ReceiveMessageResponse.builder().messages(messages).build();
  }

  @And("the account transaction queue has a message that does not have a matching account id")
  public void theAccountTransactionQueueHasAMessageThatDoesNotHaveAMatchingAccountId()
      throws JsonProcessingException {

    AccountTransaction accountTransaction = AccountTransaction.builder()
        .accountId(123456)
        .transactionDate(LocalDate.now())
        .amount(123)
        .description("Valid description")
        .build();

    String messageContent = objectMapper.writeValueAsString(accountTransaction);

    Message message = Message.builder().body(messageContent).build();

    messages.add(message);
    receiveMessageResponse = ReceiveMessageResponse.builder().messages(messages).build();

    when(accountRepository.findAllById(Collections.singletonList(123456L))).thenReturn(
        Collections.emptyList());
  }

  @And("the account transaction queue has a valid message that has a matching account id")
  public void theAccountTransactionQueueHasAValidMessageThatHasAMatchingAccountId()
      throws JsonProcessingException {
    AccountTransaction accountTransaction = AccountTransaction.builder()
        .accountId(1)
        .transactionDate(LocalDate.now())
        .amount(123)
        .description("Valid description")
        .build();

    String messageContent = objectMapper.writeValueAsString(accountTransaction);

    Message message = Message.builder().body(messageContent).build();

    messages.add(message);
    receiveMessageResponse = ReceiveMessageResponse.builder().messages(messages).build();

    when(accountRepository.findAllById(Collections.singletonList(1L))).thenReturn(
        Collections.singletonList(Account.builder().build()));
  }

  @And("the message can be deleted from the account transaction queue")
  public void theMessageCanBeDeletedFromTheAccountTransactionQueue() {
    when(sqsClient.deleteMessageBatch(any(DeleteMessageBatchRequest.class)))
        .thenReturn(DeleteMessageBatchResponse.builder().build());
  }

  @And("the application cannot save the account transaction to the data store due to a data integrity issue")
  public void theApplicationCannotSaveTheAccountTransactionToTheDataStoreDueToADataIntegrityIssue() {
    when(accountTransactionRepository.saveAll(anyList()))
        .thenThrow(new DataIntegrityViolationException("Something went wrong"));
  }

  @And("the application cannot save the account transaction to the data store due to a general exception")
  public void theApplicationCannotSaveTheAccountTransactionToTheDataStoreDueToAGeneralException() {
    when(accountTransactionRepository.saveAll(anyList()))
        .thenThrow(new DataProcessingException());
  }

  @And("the message cannot be deleted from the account transaction queue")
  public void theMessageCannotBeDeletedFromTheAccountTransactionQueue() {
    doThrow(SqsException.builder().build())
        .when(sqsClient).deleteMessageBatch(any(DeleteMessageBatchRequest.class));
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

  @Then("the application saves the account transaction")
  public void theApplicationSavesTheAccountTransaction() {
    verify(accountTransactionRepository, times(1)).saveAll(any());
  }

  @Then("the message is not deleted from the account transaction queue")
  public void theMessageIsNotDeletedFromTheAccountTransactionQueue() {
    Mockito.verify(sqsClient, times(0)).deleteMessageBatch(any(DeleteMessageBatchRequest.class));
  }
}
