package com.ebsolutions.papertrail.financialdataproviderservice.tooling;

import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction.AccountTransactionRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMemberRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.institution.InstitutionRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("mocked")
@SuppressWarnings("removal")
public class MockedIntegrationConfig {
  @MockBean
  protected AccountRepository accountRepository;
  @MockBean
  protected HouseholdMemberRepository householdMemberRepository;
  @MockBean
  protected HouseholdRepository householdRepository;
  @MockBean
  protected InstitutionRepository institutionRepository;
  @MockBean
  protected AccountTransactionRepository accountTransactionRepository;
  @MockBean
  protected UserRepository userRepository;
}
