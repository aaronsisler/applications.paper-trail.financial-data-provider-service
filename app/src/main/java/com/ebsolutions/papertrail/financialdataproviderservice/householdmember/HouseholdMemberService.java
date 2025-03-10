package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.household.Household;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdService;
import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseholdMemberService {

  private final UserService userService;
  private final HouseholdService householdService;
  private final HouseholdMemberRepository householdMemberRepository;

  public List<HouseholdMember> getAll() {
    try {
      return householdMemberRepository.findAll();
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException("Something went wrong while fetching all households");
    }
  }

  public HouseholdMember create(HouseholdMember householdMember) {
    try {
      User user = userService.get(householdMember.getUserId());
      Household household = householdService.get(householdMember.getHouseholdId());
      return householdMemberRepository.save(householdMember);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving household member");
    }
  }
}
