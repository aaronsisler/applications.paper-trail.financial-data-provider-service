package com.ebsolutions.papertrail.financialdataproviderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;

public class BaseStep {
  protected final ObjectMapper objectMapper = new ObjectMapper();
  protected RestClient restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
}
