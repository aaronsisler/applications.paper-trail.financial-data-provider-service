package com.ebsolutions.papertrail.financialdataproviderservice.tooling;

import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction.AccountTransactionRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMemberRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.institution.InstitutionRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class BaseTest {
  protected final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  protected MockMvc mockMvc;

  @TestConfiguration
  @SuppressWarnings("removal")
  /*
   * Pulling this into BaseTest since there were issues when it was a separate file
   */
  public static class BaseTestConfiguration {
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
}
