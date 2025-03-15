package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Institution;

public enum InstitutionTestData {
  NO_FOREIGN_KEY_USAGE {
    @Override
    public Institution get() {
      return Institution.builder()
          .institutionId(1)
          .name("my_bank_institution")
          .build();
    }
  },
  UPDATE {
    @Override
    public Institution get() {
      return Institution.builder()
          .institutionId(2)
          .name("update_institution")
          .build();
    }
  },
  DELETE {
    @Override
    public Institution get() {
      return Institution.builder()
          .institutionId(3)
          .name("delete_institution")
          .build();
    }
  };

  public abstract Institution get();
}