# syntax=docker/dockerfile:1
# Two stages: Maven builds the fat JAR, then only a slim JRE ships. The app is embedded Tomcat,
# so the image runs `java -jar` and needs no servlet container of its own.
# Platform contract for the kplanky.dev VPS: vps-setup/DEPLOY.md.

# 1) Build the fat JAR (maven-assembly, jar-with-dependencies).
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Dependencies resolve in their own layer, so a source-only change does not re-download them.
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline
COPY src/ src/
COPY db/ db/
RUN mvn -B -ntp -DskipTests clean package

# 2) Runtime: slim, patched, non-root.
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
RUN apk -U upgrade --no-cache \
 && addgroup -S app && adduser -S -G app app
COPY --from=build /app/target/todo-webapp-*-jar-with-dependencies.jar app.jar
# TomcatEnvironment unpacks the JSPs and Tomcat's work dir under java.io.tmpdir, which /tmp
# satisfies for the unprivileged user. Nothing is written into /app at runtime.
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
