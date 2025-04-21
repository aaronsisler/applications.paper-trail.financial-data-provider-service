package com.ebsolutions.papertrail.financialdataproviderservice.util;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class QueueMessageUtil {
  protected final String endpoint;
  protected final String queueUrl;

  public void produce(SendMessageRequest sendMessageRequest) {
    try (SqsClient sqsClient = localSqsClientInstantiation()) {

      sqsClient.sendMessage(sendMessageRequest);
    } catch (Exception exception) {
      System.out.println("Cannot retrieve messages from the queue");
      System.out.println(exception.getMessage());
      throw exception;
    }
  }

  private SqsClient localSqsClientInstantiation() {
    return SqsClient.builder()
        .region(Region.US_EAST_1)
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(staticCredentialsProvider())
        .build();
  }

  private StaticCredentialsProvider staticCredentialsProvider() {
    String awsAccessKeyId = "accessKeyId";
    String awsSecretAccessKey = "secretAccessKey";

    return StaticCredentialsProvider.create(
        AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey));
  }
}
