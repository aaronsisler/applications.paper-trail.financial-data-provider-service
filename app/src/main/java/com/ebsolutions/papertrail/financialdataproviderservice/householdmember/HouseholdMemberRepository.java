package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface HouseholdMemberRepository extends JpaRepository<HouseholdMember, Long> {
  List<HouseholdMember> findByUserId(Integer userId);

  List<HouseholdMember> findByHouseholdId(Integer householdId);
}
