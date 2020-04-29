# Build Instructions
Ensure Java 8 or later is on the path. This is true by default for the ece linux servers.

## Compile the Code
```
./gradlew build
```

## Run Tests
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