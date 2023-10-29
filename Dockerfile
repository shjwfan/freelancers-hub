FROM openjdk:17.0-jdk
ENV HOME /freelancers-hub
WORKDIR "$HOME"
ARG JAR=web/target/web-1.0-SNAPSHOT.jar
ARG JAR_PROPS=web/src/main/resources/application.yaml
COPY ${JAR} web-1.0-SNAPSHOT.jar
COPY ${JAR_PROPS} application.yml
ENTRYPOINT ["java", "-jar", "/freelancers-hub/web-1.0-SNAPSHOT.jar", "./", "--spring.config.import=file:/freelancers-hub/application.yaml"]
