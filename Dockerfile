FROM openjdk:17-oracle
WORKDIR /usr/src/app
COPY gateway/build/libs/gateway-0.0.1-SNAPSHOT.jar .
COPY gateway/src/main/resources/application.yml .
RUN sed -i 's/localhost/host.docker.internal/g' application.yml
CMD ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]
EXPOSE 8010