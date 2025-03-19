package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Account;

public enum AccountTestData {
  ACCOUNT_TRANSACTION_CREATE_ONE {
    @Override
    public Account get() {
      return Account.builder()
          .id(1)
          .institutionId(5)
          .householdMemberId(6)
          .name("account_transaction_create_name_1")
          .nickname("account_transaction_create_nickname_1")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_CREATE_TWO {
    @Override
    public Account get() {
      return Account.builder()
          .id(2)
          .institutionId(5)
          .householdMemberId(7)
          .name("account_transaction_create_name_2")
          .nickname("account_transaction_create_nickname_2")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_UPDATE {
    @Override
    public Account get() {
      return Account.builder()
          .id(2)
          .institutionId(5)
          .householdMemberId(7)
          .name("account_transaction_update_name")
          .nickname("account_transaction_update_nickname")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_DELETE {
    @Override
    public Account get() {
      return Account.builder()
          .id(2)
          .institutionId(5)
          .householdMemberId(7)
          .name("account_transaction_delete_name")
          .nickname("account_transaction_delete_nickname")
          .build();
    }
  };

  public abstract Account get();
}