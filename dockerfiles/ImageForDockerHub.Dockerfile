FROM openjdk:17-jdk-slim
COPY ./target/kk_test_task-1.0-jar-with-dependencies.jar app/kk_test_task.jar
WORKDIR app

ENTRYPOINT ["java", "-jar", "kk_test_task.jar"]