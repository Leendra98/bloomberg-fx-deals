FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY . .
RUN ./mvnw clean install
ENTRYPOINT ["java","-jar","target/ClusteredDataWarehouse-0.0.1-SNAPSHOT.jar"]
