package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
  List<AccountTransaction> findByAccountId(Integer accountId);
}
