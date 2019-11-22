FROM openjdk:8-jdk-slim
VOLUME /tpm
ADD target/itau-file-manager.jar app.jar
EXPOSE 8080
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]