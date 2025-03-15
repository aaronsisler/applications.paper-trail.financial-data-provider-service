package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.institution.Institution;
import org.junit.jupiter.api.Assertions;

public class InstitutionTestUtil {
  public static void assertExpectedAgainstActual(Institution expectedInstitution,
                                                 Institution actualInstitution) {
    Assertions.assertEquals(expectedInstitution.getInstitutionId(),
        actualInstitution.getInstitutionId());
    Assertions.assertEquals(expectedInstitution.getName(), actualInstitution.getName());
  }
}
