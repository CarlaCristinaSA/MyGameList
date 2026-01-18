FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/rest-api-with-spring-boot-*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
