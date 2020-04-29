# Build Instructions
Ensure Java 8 or later is on the path. This is true by default for the ece linux servers.

## Compile the Code
```
./gradlew build
```

## Run Tests
There are test for Zero Slack covering arrival times, required arrival times, edge slacks, zero slack, and edge deltas. Running the command below will build the test graph data and execute the tests. It should report all tests passed.

```
./gradlew test
```

## Build the Docs
The docs are provided already in the javadoc folder, `javadoc/index.html`.

You can rebuild the docs with the following command, and the generated docs will be
placed in the `build/docs` folder.

```
./gradlew javadoc
```
