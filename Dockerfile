FROM openjdk:11.0.7-jre-slim
VOLUME /tmp
EXPOSE 8080
ADD build/libs/cinema-0.0.1-SNAPSHOT.jar cinema.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cinema.jar"]