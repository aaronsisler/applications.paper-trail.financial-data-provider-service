package com.ebsolutions.papertrail.financialdataproviderservice.tooling;

import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("mocked")
@SuppressWarnings("removal")
public class MockedIntegrationConfig {
  @MockBean
  protected HouseholdRepository householdRepository;
  @MockBean
  protected UserRepository userRepository;
}
