# Overview

Find Phone Number Activation Date

# Technology

- Java 1.8
- Spark 2.2.0

# Build
Build jar file:
```
./gradlew clean build
```
# Run Spark Job
```
$SPARK_HOME/bin/spark-submit \
    --class "com.trustingsocial.test.FindActivationDate" \
    --master [SPARK_MASTER] \
    build/libs/trustingsocial-test-1.0-SNAPSHOT.jar path/to/input.csv path/to/output.csv
```

Params:
- `[SPARK_MASTER]` is spark master: e.g spark://TrongDiep:7077
- `path/to/input.csv`: input file path e.g /home/diepdt/Documents/input.csv
- `path/to/output.csv`: output file path e.g /home/diepdt/Documents/output.csv
- other spark-submit params e.g `--executor-memory 20G`, `--total-executor-cores 100`