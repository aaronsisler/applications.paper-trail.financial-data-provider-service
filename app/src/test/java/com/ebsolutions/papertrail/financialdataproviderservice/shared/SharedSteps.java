package com.ebsolutions.papertrail.financialdataproviderservice.shared;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.institution.InstitutionRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import io.cucumber.java.en.And;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SharedSteps extends BaseTest {
  protected final HouseholdRepository householdRepository;
  protected final InstitutionRepository institutionRepository;
  protected final UserRepository userRepository;

  @And("the user id provided exists in the database")
  public void theUserIdProvidedExistsInTheDatabase() {
    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(User.builder().build()));
  }

  @And("the connection to the database fails for the get user by id")
  public void theConnectionToTheDatabaseFailsForTheGetUserById() {
    doThrow(new DataProcessingException()).when(userRepository).findById(any());
  }

  @And("the connection to the database fails for the get household by id")
  public void theConnectionToTheDatabaseFailsForTheGetHouseholdById() {
    doThrow(new DataProcessingException()).when(householdRepository).findById(any());
  }

  @And("the connection to the database fails for the get institution by id")
  public void theConnectionToTheDatabaseFailsForTheGetInstitutionId() {
    doThrow(new DataProcessingException()).when(institutionRepository).findById(any());
  }
}
