package com.ebsolutions.papertrail.financialdataproviderservice.institution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
}
