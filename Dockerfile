FROM openjdk:17-oracle
WORKDIR /usr/src/app
COPY gateway/build/libs/gateway-0.0.1-SNAPSHOT.jar .
COPY gateway/src/main/java/com/medilabo/gateway/SpringCloudConfig.java .
CMD ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]
EXPOSE 8010