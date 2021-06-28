# DriverSuggestion
Driver suggestions project which uses Microservices, Docker and Kafka
- Microservices are run using maven
- Kafka, Zookeeper and MySQL are run using Docker-Compose

Step-1: 
Start Docker compose by running: 
```docker-compose up```

Step-2:
Miscroservices are currently not started by docker so these microservices needs to be started manually after docker is started.

To run the spring-boot application, need to follow some step.
Maven setup (ignore if already setup):

a. Install maven from https://maven.apache.org/download.cgi
b. Unzip maven and keep in C drive (you can keep any location. Path location will be changed accordingly).
c. Set MAVEN_HOME in system variable. enter image description here
d. Set path for maven

Build Spring Boot Project with Maven

   maven package
or

     mvn install / mvn clean install
Run Spring Boot app using Maven:

    mvn spring-boot:run
      
 ###########################################################################################################
    
 Below is the block included in docker-compose.yml to deploy microservices but it DID NOT work. Getting "ERROR: Producer Broker not available. Could not connect"
    
    driver-suggestion-container:
    image: driver-suggestion-container
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/db?autoReconnect=true&useSSL=false
      SPRING_DATASOURCE_USERNAME: "user"
      SPRING_DATASOURCE_PASSWORD: "user123"
    build:
      context: "./"
      dockerfile: "DockerFile"
    depends_on:
      - db
      - kafka
      - zookeeper
    links:
      - db
      - kafka
      - zookeeper
    networks:
      - app-tier
    
