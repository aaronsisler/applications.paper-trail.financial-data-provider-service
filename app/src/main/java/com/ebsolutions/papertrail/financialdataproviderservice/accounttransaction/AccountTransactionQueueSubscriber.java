package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;


@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
public class AccountTransactionQueueSubscriber {
  private final SqsClient sqsClient;
  private final AccountTransactionQueue accountTransactionQueue;
  private final ObjectMapper objectMapper;
  private final AccountTransactionService accountTransactionService;


  @Scheduled(fixedRate = 10000, initialDelay = 1000)
  public void consumeMessages() throws InterruptedException {
    List<Message> messages;

    try {
      ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
          .queueUrl(accountTransactionQueue.getQueueUrl())
          .maxNumberOfMessages(10)
          .waitTimeSeconds(2)
          .build();

      messages = new ArrayList<>(sqsClient.receiveMessage(receiveMessageRequest).messages());
    } catch (Exception exception) {
      log.error("Cannot retrieve messages from the queue");
      log.error(exception.getMessage());
      return;
    }

    // Exit the loop if no messages on the queue
    if (messages.isEmpty()) {
      return;
    }

    List<DeleteMessageBatchRequestEntry> deleteMessageBatchRequestEntries = new ArrayList<>();
    List<AccountTransaction> accountTransactions = new ArrayList<>();

    for (Message message : messages) {
      log.info("Message: {}", message.body());

      try {
        AccountTransaction accountTransaction =
            objectMapper.readValue(message.body(), AccountTransaction.class);
        accountTransactions.add(accountTransaction);
      } catch (JsonProcessingException e) {
        log.error("Cannot process account transaction");
        log.error(e.getMessage());
      } finally {
        deleteMessageBatchRequestEntries.add(
            DeleteMessageBatchRequestEntry.builder()
                .id(message.messageId())
                .receiptHandle(message.receiptHandle())
                .build()
        );
      }
    }

    if (accountTransactions.isEmpty()) {
      deleteQueueMessages(deleteMessageBatchRequestEntries);
      return;
    }

    try {
      accountTransactionService.createAll(accountTransactions);
    } catch (DataConstraintException dataConstraintException) {
      log.error("There is a data constraint issue");
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      log.error("There is a data issue with a missing account");
    } catch (DataProcessingException dataProcessingException) {
      log.error("Cannot save account transactions to data store - DataProcessingException");
      log.error(dataProcessingException.getMessage());
      return;
    } catch (Exception exception) {
      log.error("Cannot save account transactions to data store - Exception");
      log.error(exception.getMessage());
      return;
    }
    deleteQueueMessages(deleteMessageBatchRequestEntries);
  }

  private void deleteQueueMessages(
      List<DeleteMessageBatchRequestEntry> deleteMessageBatchRequestEntries) {
    try {
      DeleteMessageBatchRequest deleteMessageBatchRequest =
          DeleteMessageBatchRequest.builder().queueUrl(accountTransactionQueue.getQueueUrl())
              .entries(deleteMessageBatchRequestEntries)
              .build();

      sqsClient.deleteMessageBatch(deleteMessageBatchRequest);
    } catch (Exception exception) {
      log.error("Cannot delete account transactions from the queue");
      log.error(exception.getMessage());
    }
  }
}