package com.ebsolutions.papertrail.financialdataproviderservice;

import com.ebsolutions.papertrail.financialdataproviderservice.config.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;

public class BaseStep {
  protected final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
  protected RestClient restClient = RestClient
      .builder()
      .baseUrl(TestConstants.BASE_URL)
      .build();
}
