FROM openjdk:17 as build

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw -B package

FROM openjdk:17

COPY --from=build target/client-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "client-1.0-SNAPSHOT.jar"]
