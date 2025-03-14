package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.model.HouseholdMember;
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

  public static void assertExpectedAgainstCreated(HouseholdMember expectedHouseholdMember,
                                                  HouseholdMember actualHouseholdMember) {

    Assertions.assertEquals(expectedHouseholdMember.getHouseholdId(),
        actualHouseholdMember.getHouseholdId());

    Assertions.assertEquals(expectedHouseholdMember.getUserId(), actualHouseholdMember.getUserId());
  }
}
