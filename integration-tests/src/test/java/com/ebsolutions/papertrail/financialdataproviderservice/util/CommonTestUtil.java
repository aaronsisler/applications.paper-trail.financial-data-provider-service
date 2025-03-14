package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class CommonTestUtil {
  public static String isEmptyString(String value) {
    return TestConstants.EMPTY_STRING_ENUM.equals(value) ? TestConstants.EMPTY_STRING : value;
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
                .retrieve());
  }

  public static RestClient.ResponseSpec getThroughApi(RestClient restClient, String uri) {
    return checkForErrorStatusCodes(restClient
        .get()
        .uri(uri)
        .retrieve());
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
