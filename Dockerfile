FROM eclipse-temurin:17-jdk AS build

COPY . /app
WORKDIR /app

FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/hotel-app/target/hotel.war /usr/local/tomcat/webapps/ROOT.war

COPY --from=build /app/hotel-app/src/main/resources/application.properties /usr/local/tomcat/conf/

ENV CATALINA_OPTS="-Dspring.profiles.active=docker"

EXPOSE 8080

CMD ["catalina.sh", "run"]