FROM maven:4.0.0-jdk-17-slim AS build
WORKDIR /home/build
#RUN apt-get update
#RUN apt-get install openjdk-17-jdk -y
COPY . .

#RUN apt-get install maven -y
RUN mvn -f /home/build/pom.xml clean package


FROM openjdk:17-jdk-slim
WORKDIR /home/prod
EXPOSE 8080
COPY --from=build /build/target/todolist-1.0.0.jar /home/prod/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
