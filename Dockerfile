FROM openjdk:17.0.2-jdk
VOLUME /tmp
EXPOSE 8090
ADD ./target/TeleDrama-webservices-0.0.1-SNAPSHOT.jar teledrama-api1.jar
ENTRYPOINT ["java","-jar","teledrama-api1.jar"]
ENV TZ America/Lima