package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMember;
import org.junit.jupiter.api.Assertions;

public class HouseholdMemberTestUtil {
  public static void assertExpectedAgainstActual(
      HouseholdMember expectedHouseholdMember,
      HouseholdMember actualHouseholdMember) {
    Assertions.assertEquals(expectedHouseholdMember.getHouseholdMemberId(),
        actualHouseholdMember.getHouseholdMemberId());

    Assertions.assertEquals(expectedHouseholdMember.getHouseholdId(),
        actualHouseholdMember.getHouseholdId());

    Assertions.assertEquals(expectedHouseholdMember.getUserId(), actualHouseholdMember.getUserId());
  }
}
