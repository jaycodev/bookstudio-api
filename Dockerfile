FROM maven:3.9.6-eclipse-temurin-21 as builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM tomcat:8.5-jdk21
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/bookstudio-1.0.0.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
