package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Household;
import org.junit.jupiter.api.Assertions;

public class HouseholdTestUtil {
  public static void assertExpectedAgainstActual(Household expectedHousehold,
                                                 Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getId(), actualHousehold.getId());

    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }

  public static void assertExpectedAgainstCreated(Household expectedHousehold,
                                                  Household actualHousehold) {
    Assertions.assertEquals(expectedHousehold.getName(), actualHousehold.getName());
  }
}
