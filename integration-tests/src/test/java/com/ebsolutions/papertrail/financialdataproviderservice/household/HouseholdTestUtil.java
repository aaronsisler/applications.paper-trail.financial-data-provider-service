package com.ebsolutions.papertrail.financialdataproviderservice.household;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Household;
import org.junit.jupiter.api.Assertions;

public class HouseholdTestUtil {
  protected static void assertExpectedHouseholdAgainstActualHousehold(Household expectedHousehold,
                                                                      Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getHouseholdId(), actualHousehold.getHouseholdId());
    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }

  protected static void assertExpectedHouseholdAgainstCreatedHousehold(Household expectedHousehold,
                                                                       Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }
}
