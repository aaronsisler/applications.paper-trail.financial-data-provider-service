package com.ebsolutions.papertrail.financialdataproviderservice.shared;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import io.cucumber.java.en.And;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SharedSteps {
  protected final HouseholdRepository householdRepository;
  protected final UserRepository userRepository;

  @And("the connection to the database fails for the get user by id")
  public void theConnectionToTheDatabaseFailsForTheGetUserById() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    doThrow(dataProcessingException).when(userRepository).findById(any());
  }

  @And("the connection to the database fails for the get household by id")
  public void theConnectionToTheDatabaseFailsForTheGetHouseholdById() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    doThrow(dataProcessingException).when(householdRepository).findById(any());
  }
}
