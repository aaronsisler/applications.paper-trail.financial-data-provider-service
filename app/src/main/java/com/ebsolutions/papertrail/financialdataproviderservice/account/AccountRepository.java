package com.ebsolutions.papertrail.financialdataproviderservice.account;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface AccountRepository extends JpaRepository<Account, Long> {
  List<Account> findByHouseholdMemberId(Integer householdMemberId);

  List<Account> findByAccountId(Integer accountId);
}
