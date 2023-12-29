FROM debian:stable-slim as build

WORKDIR /app

RUN apt-get update && apt-get install -y curl git zip build-essential zlib1g-dev
RUN curl -s "https://get.sdkman.io" | bash

COPY . /app

ENV PATH="/root/.sdkman/candidates/java/current/bin:/root/.sdkman/candidates/scala/current/bin:/root/.sdkman/candidates/sbt/current/bin:${PATH}"
ENV JAVA_HOME="/root/.sdkman/candidates/java/current"

EXPOSE 8080

# readd nativeImage to sbt command once working
RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && \
  sdk env install && sbt clean compile nativeImage"


FROM debian:stable-slim

COPY --from=build /app/target/native-image/site /app/site

WORKDIR /app

ENV PATH="/app:${PATH}"
EXPOSE 8080

CMD ["site"]
