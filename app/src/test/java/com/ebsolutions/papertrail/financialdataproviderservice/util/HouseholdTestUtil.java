package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.household.Household;
import org.junit.jupiter.api.Assertions;

public class HouseholdTestUtil {
  public static void assertExpectedAgainstActual(Household expectedHousehold,
                                                 Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getHouseholdId(), actualHousehold.getHouseholdId());
    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }
}
