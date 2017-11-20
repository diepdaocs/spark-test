package com.trustingsocial.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class FindActivationDateTest {
  @Parameterized.Parameter(value = 0)
  public String periods;

  @Parameterized.Parameter(value = 1)
  public String expectedActDate;

  @Parameterized.Parameters(
    name = "{index} testFindActivationDate - periods {1} - expectedActDate {2}"
  )
  public static Object[][] testCases() {
    return new Object[][] {
      {"2016-01-01_2016-01-10", "2016-01-01"},
      {"2016-02-01_2016-03-01,2016-03-01_2016-05-01,2016-05-01", "2016-02-01"},
      {
        "2016-03-01_2016-05-01,2016-01-01_2016-03-01,2016-12-01,2016-09-01_2016-12-01,2016-06-01_2016-09-01",
        "2016-06-01"
      }
    };
  }

  @Test
  public void given_phonePeriods_when_findActivationDate_then_returnCorrectActivationDate()
      throws Exception {
    // When:
    String actualActDate = FindActivationDate.findActDate(periods);

    // Then:
    Assert.assertEquals(expectedActDate, actualActDate);
  }
}
