package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.HouseholdMember;

public enum HouseholdMemberTestData {
  ACCOUNT_CREATE {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(1)
          .householdId(4)
          .userId(6)
          .build();
    }
  };

  public abstract HouseholdMember get();
}