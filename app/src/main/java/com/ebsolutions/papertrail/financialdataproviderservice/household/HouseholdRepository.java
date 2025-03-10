package com.ebsolutions.papertrail.financialdataproviderservice.household;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface HouseholdRepository extends JpaRepository<Household, Long> {
}
