package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransactionQueue {
  private String queueUrl;
}
