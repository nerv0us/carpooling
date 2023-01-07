FROM openjdk:8-jdk-alpine
COPY /build/libs/carpooling-application-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar", "carpooling-application-1.0-SNAPSHOT.jar"]