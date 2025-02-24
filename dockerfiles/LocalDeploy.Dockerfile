FROM maven:3-openjdk-17
COPY ../src app/src
WORKDIR app
COPY ../pom.xml pom.xml
RUN mvn clean install

ENTRYPOINT ["java", "-jar", "target/kk_test_task-1.0-jar-with-dependencies.jar"]