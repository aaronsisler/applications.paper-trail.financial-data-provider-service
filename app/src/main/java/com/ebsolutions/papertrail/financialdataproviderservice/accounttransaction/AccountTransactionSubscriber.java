package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
public class AccountTransactionSubscriber {
  private final SqsClient sqsClient;
  private final AccountTransactionQueue accountTransactionQueue;
  private final ObjectMapper objectMapper;
  private final AccountTransactionService accountTransactionService;


  @Async
  @Scheduled(fixedRate = 1000, initialDelay = 1000)
  public void consumeMessages() throws JsonProcessingException {
    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
        .queueUrl(accountTransactionQueue.getQueueUrl())
        .maxNumberOfMessages(10)
        .waitTimeSeconds(2)
        .build();


    List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

    // Exit the loop if no messages on the queue
    if (messages.isEmpty()) {
      return;
    }

    List<DeleteMessageBatchRequestEntry> deleteMessageBatchRequestEntries = new ArrayList<>();
    List<AccountTransaction> accountTransactions = new ArrayList<>();

    for (Message message : messages) {
      System.out.println("Message");
      System.out.println(message.body());
      AccountTransaction accountTransaction =
          objectMapper.readValue(message.body(), AccountTransaction.class);
      accountTransactions.add(accountTransaction);

      deleteMessageBatchRequestEntries.add(
          DeleteMessageBatchRequestEntry.builder()
              .id(message.messageId())
              .receiptHandle(message.receiptHandle())
              .build()
      );
    }

    try {
      accountTransactionService.createAll(accountTransactions);
    } catch (Exception exception) {
      log.error("Cannot save account transactions to data store");
      log.error(exception.getMessage());
    }

    DeleteMessageBatchRequest deleteMessageBatchRequest =
        DeleteMessageBatchRequest.builder().queueUrl(accountTransactionQueue.getQueueUrl())
            .entries(deleteMessageBatchRequestEntries)
            .build();

    sqsClient.deleteMessageBatch(deleteMessageBatchRequest);
  }
}