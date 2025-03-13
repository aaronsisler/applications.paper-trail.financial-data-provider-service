package com.ebsolutions.papertrail.financialdataproviderservice.shared;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import io.cucumber.java.en.And;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SharedSteps extends BaseTest {
  protected final HouseholdRepository householdRepository;
  protected final UserRepository userRepository;

  @And("the connection to the database fails for the get user by id")
  public void theConnectionToTheDatabaseFailsForTheGetUserById() {
    doThrow(new DataProcessingException()).when(userRepository).findById(any());
  }

  @And("the connection to the database fails for the get household by id")
  public void theConnectionToTheDatabaseFailsForTheGetHouseholdById() {
    doThrow(new DataProcessingException()).when(householdRepository).findById(any());
  }
}
