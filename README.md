# activej-scala

GraalVM + Scala + ActiveJ + Twirl = <3

## Setup

This project has an SDKMAN! configuration file in the root directory. If you
have SDKMAN!, run:

```bash
sdk env install
```

The above command will install the necessary versions of Java and Scala.

## Building

Ensure `sbt` is available on your machine. The following commands are available:

```bash
# Compiles the application
sbt compile

# Builds the native version of the application
sbt nativeImage
```

## Running

To run the application, ensure port 8080 is available. Then, you can run any of
the following:

```bash
# Run compiled native image
./target/native-image/activej-scala

# Run as native image
sbt nativeImageRun

# Run normally, attached to Java
sbt compile run
```

## Performance

DISCLAIMER: my machine is most likely different from yours! Feel free to profile
the project!

I've found the performance of this to be absolutely stellar on M1 Macbook Air,
especially when running the compiled native image utilizing the MultiThreaded
server example (src/main/scala/MultiThreaded.scala). According to my benchmarks,
I've achieved an RPS of >170k with an average latency of 2ms. Very impressive
for Java and Scala!
