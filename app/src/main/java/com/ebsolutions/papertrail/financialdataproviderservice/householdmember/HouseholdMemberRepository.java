package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface HouseholdMemberRepository extends JpaRepository<HouseholdMember, Long> {
}
