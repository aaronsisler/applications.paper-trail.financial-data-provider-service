package com.ebsolutions.papertrail.financialdataproviderservice.household;

import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import org.junit.jupiter.api.Assertions;

public class HouseholdTestUtil {
  protected static void assertExpectedHouseholdAgainstActualHousehold(Household expectedHousehold,
                                                                      Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getHouseholdId(), actualHousehold.getHouseholdId());
    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }

  protected static String isEmptyString(String value) {
    return TestConstants.EMPTY_STRING_ENUM.equals(value) ? TestConstants.EMPTY_STRING : value;
  }
}
