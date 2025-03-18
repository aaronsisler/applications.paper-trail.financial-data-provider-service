package com.ebsolutions.papertrail.financialdataproviderservice.transaction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByAccountId(Integer accountId);
}
