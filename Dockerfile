FROM debian:stable-slim as build

WORKDIR /app

RUN apt-get update && apt-get install -y curl git zip build-essential zlib1g-dev
RUN curl -s "https://get.sdkman.io" | bash

COPY . /app

ENV PATH="/root/.sdkman/candidates/java/current/bin:/root/.sdkman/candidates/scala/current/bin:/root/.sdkman/candidates/sbt/current/bin:${PATH}"
ENV JAVA_HOME="/root/.sdkman/candidates/java/current"

EXPOSE 8080

# Load SDKMAN into the current shell
RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && \
  sdk env install && sbt clean compile"

# readd nativeImage to sbt command once working

# FROM debian:stable-slim

# COPY --from=build /app/target/native-image/site /app/site

# WORKDIR /app

# EXPOSE 8080

# Change me to built binary once working
CMD ["sbt", "run"]
