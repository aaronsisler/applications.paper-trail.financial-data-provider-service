package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Household;
import org.junit.jupiter.api.Assertions;

public class HouseholdTestUtil {
  public static void assertExpectedHouseholdAgainstActualHousehold(Household expectedHousehold,
                                                                   Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getHouseholdId(), actualHousehold.getHouseholdId());
    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }

  public static void assertExpectedHouseholdAgainstCreatedHousehold(Household expectedHousehold,
                                                                    Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }

  public static Household getTestDataHousehold() {
    return Household.builder()
        .householdId(1)
        .name("sisler_household")
        .build();
  }
}
