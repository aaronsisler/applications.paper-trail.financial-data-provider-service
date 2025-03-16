package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdService;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseholdMemberService {

  private final UserService userService;
  private final HouseholdService householdService;
  private final HouseholdMemberRepository householdMemberRepository;

  public List<HouseholdMember> getAllById(Integer userId) {
    try {
      return householdMemberRepository.findByUserId(userId);
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching household members");
    }
  }

  public List<HouseholdMember> getAll() {
    try {
      return householdMemberRepository.findAll();
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching household members");
    }
  }

  public HouseholdMember create(HouseholdMember householdMember) {
    try {
      if (householdMember.getId() != null) {
        throw new DataConstraintException(Collections.singletonList(
            "Household Member Id cannot be populated: ".concat(
                String.valueOf(householdMember.getId()))));
      }

      var user = userService.get(householdMember.getUserId());

      if (user.isEmpty()) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "User Id does not exist: ".concat(Integer.toString(householdMember.getUserId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      var household = householdService.get(householdMember.getHouseholdId());

      if (household.isEmpty()) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "Household Id does not exist: ".concat(
                    Integer.toString(householdMember.getHouseholdId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return householdMemberRepository.save(householdMember);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      log.error("Data Integrity:", dataIntegrityViolationException);
      throw dataIntegrityViolationException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving household member");
    }
  }
}
