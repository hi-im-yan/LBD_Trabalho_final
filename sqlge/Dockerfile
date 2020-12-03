FROM maven:3.6.3-jdk-14 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests=true

FROM openjdk:14-alpine
COPY --from=build /home/app/target/sqlge-*.jar /home/app/app.jar
ENTRYPOINT ["java", "-jar", "/home/app/app.jar"]