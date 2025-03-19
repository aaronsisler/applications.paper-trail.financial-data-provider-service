package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Institution;

public enum InstitutionTestData {
  NO_FOREIGN_KEY_USAGE {
    @Override
    public Institution get() {
      return Institution.builder()
          .id(1)
          .name("my_bank_institution")
          .build();
    }
  },
  UPDATE {
    @Override
    public Institution get() {
      return Institution.builder()
          .id(2)
          .name("update_institution")
          .build();
    }
  },
  DELETE {
    @Override
    public Institution get() {
      return Institution.builder()
          .id(3)
          .name("delete_institution")
          .build();
    }
  },
  ACCOUNT_CREATE {
    @Override
    public Institution get() {
      return Institution.builder()
          .id(4)
          .name("account_create_institution")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_CREATE {
    @Override
    public Institution get() {
      return Institution.builder()
          .id(5)
          .name("account_transaction_create_institution")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_UPDATE {
    @Override
    public Institution get() {
      return Institution.builder()
          .id(6)
          .name("account_transaction_update_institution")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_DELETE {
    @Override
    public Institution get() {
      return Institution.builder()
          .id(7)
          .name("account_transaction_delete_institution")
          .build();
    }
  };

  public abstract Institution get();
}