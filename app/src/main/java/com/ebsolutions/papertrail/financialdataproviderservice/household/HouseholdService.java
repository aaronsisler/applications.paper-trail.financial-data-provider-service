package com.ebsolutions.papertrail.financialdataproviderservice.household;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseholdService {
  private final HouseholdRepository householdRepository;

  public List<Household> getAll() {
    try {
      return householdRepository.findAll();
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException("Something went wrong while fetching all households");
    }
  }

  public Optional<Household> get(Integer householdId) {
    try {
      return householdRepository.findById(householdId.longValue());
    } catch (Exception exception) {
      log.error("Error getting by id", exception);
      throw new DataProcessingException("Something went wrong while fetching the household");
    }
  }

  public List<Household> createAll(List<Household> households) {
    try {
      if (households.isEmpty()) {
        throw new DataConstraintException(Collections.singletonList("Households cannot be empty"));
      }

      if (!households.stream()
          .filter(household -> household.getId() != null)
          .toList()
          .isEmpty()) {

        List<String> existingUserErrorMessages =
            households.stream()
                .filter(household -> household.getId() != null)
                .map(household -> "Household Id cannot be populated: ".concat(
                    Integer.toString(household.getId())))
                .toList();

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return householdRepository.saveAll(households);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving all households");
    }
  }

  public Household update(Household household) {
    try {
      if (household.getId() <= 0) {
        List<String> existingHouseholdsErrorMessages =
            Collections.singletonList("Household Id must be positive and non-zero");

        throw new DataConstraintException(existingHouseholdsErrorMessages);
      }

      boolean doesHouseholdExist =
          householdRepository.findById((long) household.getId()).isPresent();

      if (!doesHouseholdExist) {
        List<String> existingHouseholdsErrorMessages =
            Collections.singletonList(
                "Household Id does not exist: ".concat(
                    Integer.toString(household.getId())));

        throw new DataConstraintException(existingHouseholdsErrorMessages);
      }

      return householdRepository.save(household);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error saving", exception);
      throw new DataProcessingException("Something went wrong while saving the household");
    }
  }

  public void delete(Integer householdId) {
    try {
      householdRepository.deleteById(householdId.longValue());
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error deleting", exception);
      throw new DataProcessingException("Something went wrong while deleting the household");
    }
  }
}
