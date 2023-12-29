
# First stage: build the native image using the SBT & Scala base image
FROM sbtscala/scala-sbt:graalvm-community-21.0.1_1.9.7_3.3.1 as build

WORKDIR /app

# Copy your source files into the image
COPY . /app

# Run the build commands
RUN sbt clean compile nativeImage

# Second stage: start from a clean Alpine base image
FROM alpine:latest

# Copy the native executable from the build stage
COPY --from=build /app/target/native-image/site /app/site

# Set the working directory
WORKDIR /app

# Expose the port on which your app runs
EXPOSE 8080

# Set the command to run your app
CMD ["./site"]
