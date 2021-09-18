FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine
MAINTAINER I_AM
COPY target/testlimitrequest-*.jar testlimitrequest.jar
ENTRYPOINT ["java","-jar","/testlimitrequest.jar"]