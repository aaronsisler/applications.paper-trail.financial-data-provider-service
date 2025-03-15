package com.ebsolutions.papertrail.financialdataproviderservice.util;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ApiCallTestUtil {
  public static RestClient.ResponseSpec getThroughApi(RestClient restClient, String uri) {
    return checkForErrorStatusCodes(restClient
        .get()
        .uri(uri)
        .retrieve());
  }

  public static RestClient.ResponseSpec createThroughApi(RestClient restClient,
                                                         String uri,
                                                         String requestContent) {
    return
        checkForErrorStatusCodes(
            restClient
                .post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(requestContent)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
        );
  }

  public static RestClient.ResponseSpec updateThroughApi(RestClient restClient,
                                                         String uri,
                                                         String requestContent) {
    return checkForErrorStatusCodes(restClient
        .put()
        .uri(uri)
        .accept(MediaType.APPLICATION_JSON)
        .body(requestContent)
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve());
  }

  public static RestClient.ResponseSpec deleteThroughApi(RestClient restClient,
                                                         String uri) {
    return checkForErrorStatusCodes(restClient
        .delete()
        .uri(uri)
        .retrieve());
  }

  public static void checkForNoContentStatusCode(RestClient.ResponseSpec response) {
    response
        .onStatus(HttpStatusCode::is2xxSuccessful,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.NO_CONTENT, retResponse.getStatusCode()));
  }

  private static RestClient.ResponseSpec checkForErrorStatusCodes(
      RestClient.ResponseSpec response) {
    return response
        .onStatus(HttpStatusCode::is4xxClientError,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.OK, retResponse.getStatusCode()))
        .onStatus(HttpStatusCode::is5xxServerError,
            (request, retResponse)
                -> Assertions.assertEquals(HttpStatus.OK, retResponse.getStatusCode()));
  }
}
