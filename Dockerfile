FROM openjdk:17-jdk-alpine
LABEL authors="laszlo.glambusz"
COPY target/financemanager-1.0.0.jar financemanager-1.0.0.jar
ENTRYPOINT ["java","-jar","/financemanager-1.0.0.jar"]
EXPOSE 8080 9090