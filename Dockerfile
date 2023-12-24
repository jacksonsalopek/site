FROM sbtscala/scala-sbt:graalvm-community-21.0.1_1.9.7_3.3.1 as build

WORKDIR /app

COPY . /app

RUN sbt clean compile nativeImage

EXPOSE 8080

CMD ["target/native-image/activej-scala"]

