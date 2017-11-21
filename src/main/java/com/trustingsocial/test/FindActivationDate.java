package com.trustingsocial.test;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.spark.sql.functions.*;

public class FindActivationDate {

  private static final String PHONE_NUMBER_COL = "PHONE_NUMBER";
  private static final String ACT_DATE_COL = "ACTIVATION_DATE";
  private static final String DE_ACT_DATE_COL = "DEACTIVATION_DATE";
  private static final String PERIODS_COL = "PERIODS";
  private static final String RESULT_COL = "REAL_ACTIVATION_DATE";

  private static final String DATE_SEP = "_";
  private static final String PERIOD_SEP = ",";

  private static final String FUNC_FIND_ACT_DATE = "find_act_date";

  private static final String DEFAULT_RESULT_FILE = "result.csv";

  public static void main(String[] args) {
    String inputFile = args[0];
    String outputFile = DEFAULT_RESULT_FILE;
    if (args.length > 1) {
      outputFile = args[1];
    }

    SparkSession spark = SparkSession.builder().appName("FindActivationDateApp").getOrCreate();

    Dataset<Row> df = spark.read().option("header", true).csv(inputFile);
    Dataset<Row> phonePeriodsDF =
        df.groupBy(PHONE_NUMBER_COL)
            .agg(
                concat_ws(
                        PERIOD_SEP,
                        collect_list(concat_ws(DATE_SEP, col(ACT_DATE_COL), col(DE_ACT_DATE_COL))))
                    .alias(PERIODS_COL));
    spark
        .udf()
        .register(
            FUNC_FIND_ACT_DATE,
            (UDF1<String, Object>) FindActivationDate::findActDate,
            DataTypes.StringType);
    phonePeriodsDF
        .select(
            col(PHONE_NUMBER_COL), callUDF(FUNC_FIND_ACT_DATE, col(PERIODS_COL)).alias(RESULT_COL))
        .write()
        .option("header", true)
        .csv(outputFile);
  }

  static String findActDate(String periods) {
    List<String[]> lstPeriod =
        Stream.of(periods.split(PERIOD_SEP))
            .sorted()
            .map(period -> period.split(DATE_SEP))
            .collect(Collectors.toList());

    String preDeActDate = null;
    String result = lstPeriod.get(0)[0];
    for (String[] period : lstPeriod) {
      String curActDate = period[0];
      if (!Objects.equals(preDeActDate, curActDate)) {
        result = curActDate;
      }
      if (period.length > 1) {
        preDeActDate = period[1];
      }
    }

    return result;
  }
}
